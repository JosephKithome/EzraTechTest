package com.example.ezralendingapi.controllers;


import com.example.ezralendingapi.configs.MQConfig;
import com.example.ezralendingapi.dto.CustomMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController

public class MessagePublisherController {
    @Autowired
    private RabbitTemplate template;

    @Autowired MQConfig mqConfig;
    @PostMapping("/publish")
    public String publishString(@RequestBody CustomMessage message){
      message.setMessageID(UUID.randomUUID().toString());
      message.setMessageDate(new Date());
      template.convertAndSend(
              mqConfig.messageExchange1,mqConfig.routingKey,message
      );
      return "Message PUBLISHED";

    }
}
