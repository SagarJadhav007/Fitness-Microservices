package com.fitness.analyticsservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_NAME = "fitness.exchange";

    // Existing queue for per-activity analytics
    public static final String ANALYTICS_QUEUE = "analytics.queue";
    public static final String ROUTING_KEY = "activity.tracking";

    // NEW DAILY SUMMARY ROUTING
    public static final String DAILY_ROUTING_KEY = "daily.summary";

    @Bean
    public DirectExchange activityExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue analyticsQueue() {
        return new Queue(ANALYTICS_QUEUE, true);
    }

    @Bean
    public Binding analyticsBinding() {
        return BindingBuilder
                .bind(analyticsQueue())
                .to(activityExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

