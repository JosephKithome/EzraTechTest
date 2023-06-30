package com.example.ezralendingapi.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "loan_repayments")
public class LoanRepayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name="payment_date")
    private LocalDateTime date_paid;

    @Column(name="penalty")
    private BigDecimal penalty;

    @Column(name="interest")
    private BigDecimal interest;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Constructors, getters, setters, and other methods
}
