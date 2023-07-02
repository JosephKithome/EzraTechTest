package com.example.ezralendingapi.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "subscribers")
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "msisdn", nullable = false)
    private String msisdn;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = false, name = "age")
    private Integer age;

    @Column(nullable = false, name = "amount")
    private Double loanAmount =0.0;

    @Column(nullable = false, name = "credit_limit")
    private Double creditLimit =0.0;

    @Column (nullable=false, name ="loan_time")
    private Integer loanTimes =2;

    public Subscriber(long l, String number, String john, String doe, int i) {

    }

    public Subscriber() {

    }


    // Constructors, getters, setters, and other methods
}
