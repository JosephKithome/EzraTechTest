import com.example.ezralendingapi.dto.LoanRequest;
import com.example.ezralendingapi.entities.Loan;
import com.example.ezralendingapi.entities.LoanPeriod;
import com.example.ezralendingapi.entities.Subscriber;
import com.example.ezralendingapi.repository.LoanPeriodRepository;
import com.example.ezralendingapi.repository.LoanRepaymentRepository;
import com.example.ezralendingapi.repository.LoanRepository;
import com.example.ezralendingapi.repository.ProfileRepository;
import com.example.ezralendingapi.service.LoanService;
import com.example.ezralendingapi.utils.RestResponse;
import com.example.ezralendingapi.utils.SmsUtilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceTest {
    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanRepaymentRepository loanRepaymentRepository;

    @Mock
    private LoanPeriodRepository loanPeriodRepository;

    @Mock
    private SmsUtilityService smsUtilityService;

    private LoanService loanService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        loanService = new LoanService(profileRepository, loanRepository, loanRepaymentRepository, loanPeriodRepository, smsUtilityService);
    }

    @Test
    public void testCreateLoan_Success() {
        // Mock the dependencies
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setMsisdn("1234567890");
        loanRequest.setLoanPeriod(3);
        loanRequest.setAmount(BigDecimal.valueOf(1000));

        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn("1234567890");

        LoanPeriod loanPeriod = new LoanPeriod();
        loanPeriod.setPeriod(3);

        when(profileRepository.findByMsisdn(loanRequest.getMsisdn())).thenReturn(Optional.of(subscriber));
        when(loanPeriodRepository.findByPeriod(loanRequest.getLoanPeriod())).thenReturn(Optional.of(loanPeriod));
        when(profileRepository.getNumberOfLoanTimes()).thenReturn(2);

        // Invoke the method
        RestResponse response = loanService.createLoan(loanRequest);

        // Verify the result
        assertEquals(HttpStatus.OK, HttpStatus.OK);
        assertEquals("You have requested for a loan successfully", response.getBody().message);
        assertNotNull(response.getBody().payload);

        // Verify the interactions with mocked objects
        verify(loanRepository, times(1)).save(any(Loan.class));
        verify(profileRepository, times(1)).save(subscriber);
        verify(smsUtilityService, times(1)).sendMessage(eq("1234567890"), anyString());
    }
}
