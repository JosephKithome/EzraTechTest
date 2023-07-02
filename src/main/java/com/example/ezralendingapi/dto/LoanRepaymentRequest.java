package com.example.ezralendingapi.dto;

import com.example.ezralendingapi.entities.Loan;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LoanRepaymentRequest {

    public Long loan;
    public BigDecimal amount;
    public BigDecimal penalty;

}
