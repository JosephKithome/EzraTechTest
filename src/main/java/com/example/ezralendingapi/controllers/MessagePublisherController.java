package com.example.ezralendingapi.controllers;


import com.example.ezralendingapi.configs.MQConfig;
import com.example.ezralendingapi.configs.producer.RabbitMQJsonProducer;
import com.example.ezralendingapi.configs.producer.RabbitMQProducer;
import com.example.ezralendingapi.dto.CustomMessage;
import com.example.ezralendingapi.utils.RestResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController

public class MessagePublisherController {
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private RabbitMQJsonProducer rabbitMQJsonProducer;

    @Autowired MQConfig mqConfig;
    @PostMapping("/publish")
    public String publishString(@RequestBody CustomMessage message){
      message.setMessageID(UUID.randomUUID().toString());
      message.setMessageDate(new Date());
      template.convertAndSend(
              mqConfig.messageExchange,mqConfig.routingKey,message
      );
      return "Message PUBLISHED";

    }

    @PostMapping("/")
    public String getData(@RequestParam String message){
       rabbitMQProducer.sendMessage(message);
        return "Message Send to RabbitMQ...";

    }

    @PostMapping("/sendMessage")
    private RestResponse sendJsonData(@RequestBody CustomMessage message){
        message.setMessageID(UUID.randomUUID().toString());
        message.setMessageDate(new Date());
        return rabbitMQJsonProducer.sendJsonMessage(message);
    }
}
