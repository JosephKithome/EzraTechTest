package com.example.ezralendingapi.controllers;

import com.example.ezralendingapi.configs.MQConfig;
import com.example.ezralendingapi.dto.LoanPeriod;
import com.example.ezralendingapi.dto.LoanRepaymentRequest;
import com.example.ezralendingapi.dto.LoanRequest;
import com.example.ezralendingapi.service.LoanService;
import com.example.ezralendingapi.utils.RestResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/loan")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    MQConfig mqConfig;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/request")
    public RestResponse createLoan(@RequestBody LoanRequest req){
        template.convertAndSend(
                mqConfig.messageExchange,
                mqConfig.routingKey,
                req
        );
        return loanService.createLoan(req);
    }

    @PostMapping("/approve/{id}")
    public RestResponse approveLoan(@PathVariable("id") Long id, String msisdn){
        return loanService.approveLoan(id,msisdn);
    }


    @PostMapping("/repay")
    private RestResponse payLoan(@RequestBody LoanRepaymentRequest req){
        return loanService.payLoan(req);
    }
    @PostMapping("/configure/period")
    public RestResponse configureLoanPeriod(@RequestBody LoanPeriod req){
        return loanService.configureLoanPeriods(req);
    }
}
