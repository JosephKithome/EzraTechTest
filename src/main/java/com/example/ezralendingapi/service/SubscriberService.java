package com.example.ezralendingapi.service;


import com.example.ezralendingapi.dto.SubscriberRequest;
import com.example.ezralendingapi.entities.Subscriber;
import com.example.ezralendingapi.repository.ProfileRepository;
import com.example.ezralendingapi.repository.SubscriberRepository;
import com.example.ezralendingapi.utils.LogHelper;
import com.example.ezralendingapi.utils.ResponseObject;
import com.example.ezralendingapi.utils.RestResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class SubscriberService {

    private final ProfileRepository profileRepository;

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    public SubscriberService(SubscriberRepository subscriberRepository, ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public RestResponse createSubscriber(SubscriberRequest request){
        ResponseObject resp = new ResponseObject();
        resp.message = String.valueOf(HttpStatus.OK.is2xxSuccessful());
        HttpStatus status = HttpStatus.BAD_REQUEST;

        try{

            Optional<Subscriber> existingSubscriber =  profileRepository.findByMsisdn(request.msisdn);
            if(existingSubscriber.isPresent()) throw new Exception("Subscriber "+ request.msisdn + "exists");

            Subscriber sub = new Subscriber();
            sub.setAge(request.age);
            sub.setMsisdn(request.msisdn);
            sub.setFirstName(request.firstName);
            sub.setLastName(request.lastName);

            profileRepository.save(sub);
            resp.message = "Subscriber created successfully";
            resp.payload = sub;
            status = HttpStatus.CREATED;

        }catch(Exception e){
            LogHelper.info(e.getMessage());
            resp.message = e.getMessage();
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return  new RestResponse (resp, status);

    }

    /**
     * Retrieves the list of the subscribers as saved in database
     * @returns RestResponse which prints out the response and the
     * status based on the results retrieved from the database
     * */

    public RestResponse listSubscribers(){

        ResponseObject resp = new ResponseObject();
        resp.message = String.valueOf(HttpStatus.OK.is2xxSuccessful());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        try{

            List<Subscriber>  subscribers = profileRepository.findAll();
            LogHelper.debug(mapper.writeValueAsString(subscribers));
            resp.payload = subscribers;
            resp.message = "Success";
            resp.recordCount = subscribers.size();
            status = HttpStatus.OK;
        }catch (Exception e){
            try {
                LogHelper.debug(mapper.writeValueAsString(e.getMessage()));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return new RestResponse(resp,status);
    }
    public RestResponse updateSubscriber(Long id, SubscriberRequest req){
        ResponseObject resp = new ResponseObject();
        resp.message = String.valueOf(HttpStatus.OK.is2xxSuccessful());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        try{
            Subscriber sub = profileRepository.getReferenceById(id);
            sub.setMsisdn(req.msisdn);
            sub.setLastName(req.lastName);
            sub.setFirstName(req.firstName);
            sub.setAge(req.age);

            profileRepository.save(sub);
            resp.message ="Success";
            status = HttpStatus.OK;
        }
        catch(Exception e){
            resp.message = e.getMessage();
            status = HttpStatus.EXPECTATION_FAILED;
        }

        return new RestResponse(resp,status);
    }

    public RestResponse getSubscriberDetails(Long id) {

        ResponseObject resp = new ResponseObject();
        resp.message = String.valueOf(HttpStatus.OK.is2xxSuccessful());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        try{
            Optional<Subscriber> subscriber = profileRepository.findById(id);
            if(subscriber.isPresent()){
                resp.payload = subscriber.get();
                resp.message = "Subscriber details";
                status = HttpStatus.OK;
            }else{
                resp.message = "No subscriber with "+ id + " was found";
                resp.payload = "";
                status = HttpStatus.BAD_REQUEST;
            }

        }
        catch(Exception e){
            resp.message = e.getMessage();
            status = HttpStatus.EXPECTATION_FAILED;
        }

        return new RestResponse(resp,status);
    }
}
