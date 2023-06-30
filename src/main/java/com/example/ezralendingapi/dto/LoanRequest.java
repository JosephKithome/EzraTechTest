package com.example.ezralendingapi.dto;

import com.example.ezralendingapi.entities.LoanPeriod;
import com.example.ezralendingapi.entities.LoanStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LoanRequest {
    public String msisdn;
    public BigDecimal amount;

    public Float interest;

    public Float penalty;
    public LoanStatus status;

    public Integer loanPeriod;
    public LocalDateTime createdAt;

    public void setDueDate(LocalDateTime localDateTime) {
    }
}
