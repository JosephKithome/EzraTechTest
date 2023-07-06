package com.example.ezralendingapi.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public String messageExchange1 = "MESSAGE_EXCHANGE";
    public String messageQueue1 = "MESSAGE_QUEUE";
    public String routingKey = "ROUTING_KEY";
    @Bean
    public Queue queue(){

        Queue messageQueue = new Queue(messageQueue1);
        return messageQueue;
    }

    @Bean
    public TopicExchange topicExchange(){

        TopicExchange messageExchange = new TopicExchange(messageExchange1);
        return messageExchange;
    }

    @Bean
    public Binding binding (Queue queue, TopicExchange topicExchange){

        return BindingBuilder
                .bind(queue)
                .to(topicExchange)
                .with(routingKey);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        return rabbitTemplate;


    }
}
