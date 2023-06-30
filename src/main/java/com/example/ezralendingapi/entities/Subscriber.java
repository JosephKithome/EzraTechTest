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

    public Subscriber(long l, String number, String john, String doe, int i) {

    }

    public Subscriber() {

    }

    // Constructors, getters, setters, and other methods
}
