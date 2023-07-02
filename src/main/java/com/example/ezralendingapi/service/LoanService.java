package com.example.ezralendingapi.service;

import com.example.ezralendingapi.dto.LoanRepaymentRequest;
import com.example.ezralendingapi.dto.LoanRequest;
import com.example.ezralendingapi.entities.*;
import com.example.ezralendingapi.repository.LoanPeriodRepository;
import com.example.ezralendingapi.repository.LoanRepaymentRepository;
import com.example.ezralendingapi.repository.LoanRepository;
import com.example.ezralendingapi.repository.ProfileRepository;
import com.example.ezralendingapi.utils.LogHelper;
import com.example.ezralendingapi.utils.ResponseObject;
import com.example.ezralendingapi.utils.RestResponse;
import com.example.ezralendingapi.utils.SmsUtilityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private final ProfileRepository profileRepository;
    private final LoanRepository loanRepository;

    private final LoanRepaymentRepository loanRepaymentRepository;

    private final LoanPeriodRepository loanPeriodRepository;

    private final SmsUtilityService smsUtilityService;

    private ObjectMapper mapper = new ObjectMapper();


    public LoanService(
            ProfileRepository profileRepository,
            LoanRepository loanRepository,
            LoanRepaymentRepository loanRepaymentRepository, LoanPeriodRepository loanPeriodRepository,
            SmsUtilityService smsUtilityService)
    {
        this.profileRepository = profileRepository;
        this.loanRepository = loanRepository;
        this.loanRepaymentRepository = loanRepaymentRepository;
        this.loanPeriodRepository = loanPeriodRepository;
        this.smsUtilityService = smsUtilityService;
    }

    public RestResponse createLoan(LoanRequest req ) {
        ResponseObject resp = new ResponseObject();
        resp.message = String.valueOf(HttpStatus.OK.is2xxSuccessful());
        HttpStatus status = HttpStatus.BAD_REQUEST;
       try{
           Subscriber subscriber = profileRepository.findByMsisdn(req.msisdn)
                   .orElseThrow(() -> new RuntimeException("Subscriber not found"));

           Optional<LoanPeriod> period = loanPeriodRepository.findByPeriod(req.loanPeriod);
           if(period.isPresent()){
               //Get and update the loanTimes in subscribers table
               Integer time = profileRepository.getNumberOfLoanTimes();
               if(time > 0){
                   LogHelper.info("We are requesting for a loan");
                   Loan loan = new Loan();

                   loan.setDue_date(LocalDateTime.now().plusMonths(req.loanPeriod));
                   loan.setSubscriber(subscriber);
                   loan.setAmount(req.amount);
                   loan.setStatus(LoanStatus.ACTIVE);
                   loan.setCreatedAt(LocalDateTime.now());
                   loan.setLoanPeriod(req.loanPeriod);
                   loanRepository.save(loan);

                   //Update the number of times remaining for a user to request for another loan
                   subscriber.setLoanTimes(time-1);
                   subscriber.setLoanAmount(subscriber.getLoanAmount()+(req.amount.toBigInteger().doubleValue()));
                   profileRepository.save(subscriber);

                   //Send A Message
                   smsUtilityService.sendMessage(subscriber.getMsisdn(), "You requested for a loan ");

                   resp.message = "You have requested for a loan successfully";
                   resp.payload = loan;
                   status = HttpStatus.OK;
               }else{
                   throw new Exception("You have reached the maximum amount of times.\n" +
                           "Please Pay the previous loans first for you to be allowed to borrow again");
               }

           }else{
               List<LoanPeriod> loanPeriod = loanPeriodRepository.findAll();
               throw new Exception("You can apply for a loan payable between periods of " + loanPeriod);
           }

       }catch (Exception e){
           resp.message = e.getMessage();

       }

       return new  RestResponse(resp,status);
    }

    /**
     * Decides whether to grant or deny the loan
     * to the subscriber based on the credit worthiness of the subscriber
     * TODO("Come with a plan on what determines credit worthiness of subscribers")
     * */
    public RestResponse approveLoan(Long id, String msisdn) {
        ResponseObject resp = new ResponseObject();
        resp.message = String.valueOf(HttpStatus.OK.is2xxSuccessful());
        HttpStatus status = HttpStatus.BAD_REQUEST;

        try{
            Loan loan = loanRepository.findSubscriberLoanById(id);
            //check credit worthiness of the request subscriber
            Double creditLimit  = profileRepository.getCreditLimitByMsisdn(msisdn);

            if(loan.getStatus() == LoanStatus.ACTIVE){
                if(loan.getAmount().doubleValue() > creditLimit){
                    loan.setStatus(LoanStatus.DECLINED);
                    loanRepository.save(loan);
                    throw new Exception("This loan cannot be approved since it  has exceeded credit worthiness of the subscriber");
                }else{
                    loan.setIs_approved(true);
                    loan.setStatus(LoanStatus.APPROVED);
                    loanRepository.save(loan);
                    resp.message = "Sucess";
                    resp.payload ="Your loan was approved and it will be credited in your account shortly";

                    //Send the message
                    this.smsUtilityService.sendMessage(msisdn, resp.payload.toString());
                    LogHelper.info("We are approving the loan for ....... "+ msisdn + " " +loan.getAmount());

                }
            }else{
                resp.message = "Failed to approve the loan since it is in "+loan.getStatus().toString();
                throw new Exception("A loan of status "+ loan.getStatus()+ " Cannot be approved");

            }

        }catch (Exception e){
            resp.message = e.getMessage();
            status = HttpStatus.EXPECTATION_FAILED;

        }

        return new RestResponse(resp,status);
    }
    public RestResponse payLoan(LoanRepaymentRequest req){
        ResponseObject resp = new ResponseObject();
        resp.message = String.valueOf(HttpStatus.OK.is2xxSuccessful());
        HttpStatus status = HttpStatus.BAD_REQUEST;

        try{

            //Get the loan we're about to pay for
            Loan loan = loanRepository.findSubscriberLoanById(req.loan);
            //calculate interest

            // Check if the loan has been paid for
            if(loan.getIs_Cleared() || loan.getStatus()==LoanStatus.DECLINED) throw new Exception("This loan has been  cleared");

            if(loan.getDue_date().isAfter(LocalDateTime.now())){

                Integer period = calculateNumberOfMonths(loan.getCreatedAt(),LocalDateTime.now());
                LogHelper.info("The period for the loan is " + period);


                LoanRepayment loanRep = new LoanRepayment();
                loanRep.setLoan(loanRepository.findById(req.loan).get());
                loanRep.setInterest(calculateLoanInterest(loan.getAmount(),period));

                LogHelper.info("The Interest earned is"+loanRep.getInterest());

                LogHelper.info("The Loan Details are........."+mapper.writeValueAsString(loan));

                loanRep.setCreatedAt(LocalDateTime.now());
                loanRep.setAmount(loan.getAmount());

                //Adjust the amount in the loans table
                    BigDecimal bigDecimalValue = new BigDecimal(String.valueOf(loanRep.getAmount()));

                    BigDecimal result = loan.getAmount().subtract(BigDecimal.valueOf(bigDecimalValue.doubleValue()));
                LogHelper.info("We are subtraction "+ loan.getAmount() +" from " +bigDecimalValue);
                    LogHelper.info("AFter conversion we get "+ result);
                loanRepaymentRepository.save(loanRep);

                //Update the Loans Table with the required data

            }else  if(loan.getDue_date().isBefore(LocalDateTime.now())
                    &&  (calculateNumberOfMonths(loan.getCreatedAt(),LocalDateTime.now())
                    >loan.loanPeriod) ){

            }
        }catch (Exception e){
            resp.message = e.getMessage();
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return new  RestResponse(resp,status);
    }
    public RestResponse configureLoanPeriods(com.example.ezralendingapi.dto.LoanPeriod request){
        ResponseObject resp = new ResponseObject();
        resp.message = String.valueOf(HttpStatus.OK.is2xxSuccessful());
        HttpStatus status = HttpStatus.BAD_REQUEST;

        try{

            Optional<LoanPeriod> period =  loanPeriodRepository.findByPeriod(request.period);
            if(period.isPresent()) throw new Exception("Period "+ request.period + " exists");

            LoanPeriod p = new LoanPeriod();
            p.setPeriod(request.period);
            p.setDescription(request.description);
            loanPeriodRepository.save(p);
            resp.message = "Period created successfully";
            status = HttpStatus.CREATED;

        }catch(Exception e){
            LogHelper.info(e.getMessage());
            resp.message = e.getMessage();
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return  new RestResponse (resp, status);

    }


    public int calculateNumberOfMonths(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        long months = ChronoUnit.MONTHS.between(startDateTime, endDateTime);
        return Math.toIntExact(months);
    }
        public void sweepAndClearLoans(int loanAgeInMonths) {
            LocalDateTime cutoffDateTime = LocalDateTime.now().minusMonths(loanAgeInMonths);
            List<Loan> defaultedLoans = loanRepository.findDefaultedLoans(cutoffDateTime);

            for (Loan loan : defaultedLoans) {
                // Perform any necessary operations to clear the loan
                loan.setStatus(LoanStatus.SWEPT);
                loanRepository.save(loan);
            }
        }


    @Scheduled(cron = "0 0 0 1 * ?") // Runs at midnight on the 1st day of every month
    public void performLoanSweeping() {
        int loanAgeInMonths = 6; // Configure the loan age for clearing
        sweepAndClearLoans(loanAgeInMonths);
    }

    public BigDecimal calculateLoanInterest(BigDecimal loanAmount, int loanPeriod) {
        // Define your interest calculation logic here
        BigDecimal interestRate = BigDecimal.valueOf(0.1); // Assuming interest rate of 10%
        BigDecimal interest = loanAmount.multiply(interestRate).multiply(BigDecimal.valueOf(loanPeriod));

        return interest;
    }


}
