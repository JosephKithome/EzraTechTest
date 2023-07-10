package com.example.ezralendingapi.configs.producer;


import com.example.ezralendingapi.utils.LogHelper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    @Value("${rabbitmq.exchange.name}")
    public String messageExchange;

    @Value("${rabbitmq.queue.name}")
    private String messageQueue;

    @Value("${rabbitmq.queue.routing_key}")
    public String routingKey;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message){
        LogHelper.info("Sending Message.... "+message);
        rabbitTemplate.convertAndSend(messageExchange,routingKey, message);

    }

}
