package com.example.ezralendingapi.service;

import com.example.ezralendingapi.dto.SubscriberRequest;
import com.example.ezralendingapi.entities.Subscriber;
import com.example.ezralendingapi.repository.ProfileRepository;
import com.example.ezralendingapi.utils.RestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SubscriberServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private SubscriberService subscriberService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSubscriber_Success() {
        // Mock data
        SubscriberRequest subscriberRequest = new SubscriberRequest();
        subscriberRequest.setMsisdn("1234567890");
        subscriberRequest.setFirstName("John");
        subscriberRequest.setLastName("Doe");
        subscriberRequest.setAge(30);

        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn("1234567890");
        subscriber.setFirstName("John");
        subscriber.setLastName("Doe");
        subscriber.setAge(30);

        // Mock the behavior of repository methods
        when(profileRepository.findByMsisdn("1234567890")).thenReturn(Optional.empty());
        when(profileRepository.save(any(Subscriber.class))).thenReturn(subscriber);

        // Perform the createSubscriber operation
        RestResponse response = subscriberService.createSubscriber(subscriberRequest);

        // Verify the response
        assertEquals("Subscriber created successfully", Objects.requireNonNull(response.getBody()).message);
        assertEquals(subscriber, response.getBody().payload);
    }

    @Test
    public void testCreateSubscriber_SubscriberAlreadyExists() {
        // Mock data
        SubscriberRequest subscriberRequest = new SubscriberRequest();
        subscriberRequest.setMsisdn("1234567890");

        Subscriber existingSubscriber = new Subscriber();
        existingSubscriber.setId(1);
        existingSubscriber.setMsisdn("1234567890");

        // Mock the behavior of repository methods
        when(profileRepository.findByMsisdn("1234567890")).thenReturn(Optional.of(existingSubscriber));

        // Perform the createSubscriber operation
        RestResponse response = subscriberService.createSubscriber(subscriberRequest);

        // Verify the response
        assertEquals("Subscriber 1234567890exists", Objects.requireNonNull(response.getBody()).message);
    }


    @Test
    public void testListSubscribers_Success() {
        // Mock data
        List<Subscriber> subscribers = new ArrayList<>();
        subscribers.add(new Subscriber(1L, "1234567890", "John", "Doe", 30));
        subscribers.add(new Subscriber(2L, "9876543210", "Jane", "Smith", 25));

        // Mock the behavior of repository methods
        when(profileRepository.findAll()).thenReturn(subscribers);

        // Perform the listSubscribers operation
        RestResponse response = subscriberService.listSubscribers();

        // Verify the response
        assertEquals("Success", Objects.requireNonNull(response.getBody()).message);
        assertEquals(subscribers, response.getBody().payload);
        assertEquals(subscribers.size(), response.getBody().recordCount);
    }


}
