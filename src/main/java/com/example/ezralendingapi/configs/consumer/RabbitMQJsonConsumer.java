package com.example.ezralendingapi.configs.consumer;


import com.example.ezralendingapi.dto.CustomMessage;
import com.example.ezralendingapi.utils.LogHelper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQJsonConsumer {

    @RabbitListener(queues = {"${rabbitmq.jsonQueue.name}"})
    public void consumeJsonMessage(CustomMessage message) {

        LogHelper.info("We received from Json Message from RabbitMQ....." + message.toString());
    }
}
