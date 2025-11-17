package com.fitness.aiservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String MAIN_EXCHANGE = "fitness.exchange";
    public static final String DAILY_SUMMARY_QUEUE = "daily.summary.queue";
    public static final String DAILY_ROUTING_KEY = "daily.summary";

    // Existing queues (keep them)
    @Bean
    public Queue activityQueue() {
        return new Queue("activity.queue", true);
    }

    @Bean
    public Queue goalsQueue() {
        return new Queue("goals.queue", true);
    }

    @Bean
    public DirectExchange fitnessExchange() {
        return new DirectExchange(MAIN_EXCHANGE);
    }

    // Existing bindings
    @Bean
    public Binding activityBinding(Queue activityQueue, DirectExchange fitnessExchange) {
        return BindingBuilder.bind(activityQueue).to(fitnessExchange).with("activity.tracking");
    }

    @Bean
    public Binding goalsBinding(Queue goalsQueue, DirectExchange fitnessExchange) {
        return BindingBuilder.bind(goalsQueue).to(fitnessExchange).with("goals");
    }

    // NEW DAILY SUMMARY QUEUE
    @Bean
    public Queue dailySummaryQueue() {
        return new Queue(DAILY_SUMMARY_QUEUE, true);
    }

    @Bean
    public Binding dailySummaryBinding(Queue dailySummaryQueue, DirectExchange fitnessExchange) {
        return BindingBuilder
                .bind(dailySummaryQueue)
                .to(fitnessExchange)
                .with(DAILY_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
