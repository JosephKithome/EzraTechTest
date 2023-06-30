package com.example.ezralendingapi.controllers;

import com.example.ezralendingapi.dto.SubscriberRequest;
import com.example.ezralendingapi.service.SubscriberService;
import com.example.ezralendingapi.utils.RestResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/subscriber")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/create")
    private RestResponse createSubscriber(SubscriberRequest req){

        return subscriberService.createSubscriber(req);

    }

    @GetMapping("/list")
    private RestResponse listSubscribers(){

        return subscriberService.listSubscribers();

    }

    @PostMapping("/update/{id}")
    private RestResponse updateSubscriberDetails(@PathVariable("id") Long id, @RequestBody SubscriberRequest req){

        return subscriberService.updateSubscriber(id,req);

    }
    @GetMapping("/{id}")
    private RestResponse getSubscriberDetails(@PathVariable("id") Long id){
        return subscriberService.getSubscriberDetails(id);
    }
}
