package com.example.ezralendingapi.service;

import com.example.ezralendingapi.dto.LoanRepaymentRequest;
import com.example.ezralendingapi.dto.LoanRequest;
import com.example.ezralendingapi.entities.*;
import com.example.ezralendingapi.repository.LoanPeriodRepository;
import com.example.ezralendingapi.repository.LoanRepository;
import com.example.ezralendingapi.repository.ProfileRepository;
import com.example.ezralendingapi.utils.ResponseObject;
import com.example.ezralendingapi.utils.RestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LoanServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanPeriodRepository loanPeriodRepository;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLoan_Success() {
        // Mock data
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setMsisdn("1234567890");
        loanRequest.setAmount(BigDecimal.valueOf(1000));
        loanRequest.setLoanPeriod(6);
        loanRequest.setCreatedAt(LocalDateTime.now());
        loanRequest.setDueDate(LocalDateTime.now().plusMonths(6));

        Subscriber subscriber = new Subscriber();
        subscriber.setId(1);
        subscriber.setMsisdn("1234567890");

        LoanPeriod loanPeriod = new LoanPeriod();
        loanPeriod.setId(1);
        loanPeriod.setPeriod(6);

        Loan loan = new Loan();
        loan.setSubscriber(subscriber);
        loan.setAmount(BigDecimal.valueOf(1000));
        loan.setLoanPeriod(6);
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setCreatedAt(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusMonths(6));

        // Mock the behavior of repository methods
        when(profileRepository.findByMsisdn("1234567890")).thenReturn(Optional.of(subscriber));
        when(loanPeriodRepository.findByPeriod(6)).thenReturn(Optional.of(loanPeriod));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // Perform the createLoan operation
        RestResponse response = loanService.createLoan(loanRequest);

        // Verify the response
        assertEquals("You have requested for a loan successfully", Objects.requireNonNull(response.getBody()).message);
        assertEquals(loan, response.getBody().payload);
    }
}