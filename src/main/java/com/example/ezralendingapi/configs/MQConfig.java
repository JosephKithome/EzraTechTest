package com.example.ezralendingapi.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${rabbitmq.exchange.name}")
    public String messageExchange;

    @Value("${rabbitmq.json_exchange.name}")
    public String jsonMessageExchange;


    @Value("${rabbitmq.queue.name}")
    private String messageQueue;

    @Value("${rabbitmq.jsonQueue.name}")
    private String jsonQueue;


    @Value("${rabbitmq.queue.routing_key}")
    public String routingKey;

    @Value("${rabbitmq.queue.jsonRouting_key}")
    public String jsonRoutingKey;



    @Bean
    public Queue queue(){

        return new Queue(messageQueue);
    }


    /**
     * Serializes json data to be inserted into a queue
     * {@link com.example.ezralendingapi.configs.producer.RabbitMQProducer }
     * */
    @Bean
    public Queue jsonQueue(){

        return new Queue(jsonQueue);
    }
    /**
     * @author Kithome Joseph
     * RabbitMQ exchange config
     * */
    @Bean
    public TopicExchange topicExchange(){

        return new TopicExchange(messageExchange);
    }

    @Bean
    public TopicExchange jsonTopicExchange(){

        return new TopicExchange(jsonMessageExchange);
    }

    /**
     * Binding between Queue and exchange using the binding Key
     * */

    @Bean
    public Binding binding (Queue queue, TopicExchange topicExchange){

        return BindingBuilder
                .bind(queue)
                .to(topicExchange)
                .with(routingKey);
    }

    @Bean
    public Binding jsonBinding (){

        return BindingBuilder
                .bind(jsonQueue())
                .to(jsonTopicExchange())
                .with(jsonRoutingKey);
    }
    /**
     * Manages data conversion to Pojo classes
     * */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Spring boot autoconfigures ConnectionFactory,RabbitTemplate and RabbitAdmin
     * */
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());

        return rabbitTemplate;


    }
}
