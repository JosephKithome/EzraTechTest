package com.example.ezralendingapi.controllers;

import com.example.ezralendingapi.dto.LoanPeriod;
import com.example.ezralendingapi.dto.LoanRequest;
import com.example.ezralendingapi.service.LoanService;
import com.example.ezralendingapi.utils.RestResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/loan")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/request")
    public RestResponse createLoan(@RequestBody LoanRequest req){
        return loanService.createLoan(req);
    }
    @PostMapping("/configure/period")
    public RestResponse configureLoanPeriod(@RequestBody LoanPeriod req){
        return loanService.configureLoanPeriods(req);
    }
}
