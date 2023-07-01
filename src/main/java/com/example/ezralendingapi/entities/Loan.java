package com.example.ezralendingapi.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private Subscriber subscriber;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "loan_period")
    public Integer loanPeriod;

    @Column(name = "is_approved")
    public Boolean is_approved =false;

    @Column(name = "is_declined")
    public Boolean is_declined = false;

    @Column(name = "is_overdue")
    public Boolean is_overdue = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('active', 'repaid') DEFAULT 'active'")
    private LoanStatus status;

    @Column(name="due_date")
    private LocalDateTime due_date;

    @Column
    private  Boolean is_Cleared =false;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    public void setDueDate(LocalDateTime localDateTime) {
    }

    // Constructors, getters, setters, and other methods
}
