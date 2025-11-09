package com.fitness.userservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_NAME = "fitness.exchange";
    public static final String GOALS_QUEUE = "goals.queue";
    public static final String GOALS_ROUTING_KEY = "goals";

    @Bean
    public DirectExchange fitnessExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue goalsQueue() {
        return new Queue(GOALS_QUEUE, true);
    }

    @Bean
    public Binding goalsBinding(Queue goalsQueue, DirectExchange fitnessExchange) {
        return BindingBuilder.bind(goalsQueue).to(fitnessExchange).with(GOALS_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
