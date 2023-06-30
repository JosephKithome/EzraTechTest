package com.example.ezralendingapi.dto;


import lombok.Data;

@Data
public class SubscriberRequest {
    public String msisdn;
    public String firstName;
    public String lastName;
    public Integer age;
}
