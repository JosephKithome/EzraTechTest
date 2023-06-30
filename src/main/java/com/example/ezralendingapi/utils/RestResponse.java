package com.example.ezralendingapi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestResponse extends ResponseEntity<ResponseObject> {

    public RestResponse(ResponseObject body, HttpStatus status) {
        super(body, status);
    }
}