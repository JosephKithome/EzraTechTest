package com.example.ezralendingapi.configs.producer;


import com.example.ezralendingapi.dto.CustomMessage;
import com.example.ezralendingapi.utils.LogHelper;
import com.example.ezralendingapi.utils.ResponseObject;
import com.example.ezralendingapi.utils.RestResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQJsonProducer {

    @Value("${rabbitmq.json_exchange.name}")
    public String jsonMessageExchange;

    @Value("${rabbitmq.jsonQueue.name}")
    private String jsonQueue;

    @Value("${rabbitmq.queue.jsonRouting_key}")
    public String jsonRoutingKey;


    private RabbitTemplate rabbitTemplate;

    public RabbitMQJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public RestResponse sendJsonMessage(CustomMessage message){
        LogHelper.info("Sending Json data to Rabbit MQ...."+ message.toString());

        ResponseObject resp = new ResponseObject();
        resp.message = String.valueOf(HttpStatus.OK.is2xxSuccessful());
        HttpStatus status = HttpStatus.BAD_REQUEST;


        try{
            rabbitTemplate.convertAndSend(jsonMessageExchange,jsonRoutingKey, message);
            resp.message = "Data Send to RabbitMQ....";
            resp.payload =message;
        }catch (Exception e){
            resp.message = e.getMessage();
        }

        return new  RestResponse(resp, status);
    }
}
