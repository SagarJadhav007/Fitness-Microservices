package com.example.activityservice.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_NAME = "fitness.exchange";
    public static final String ACTIVITY_QUEUE = "activity.queue";      // Recommendation
    public static final String ANALYTICS_QUEUE = "analytics.queue";    // Analytics
    public static final String ROUTING_KEY = "activity.tracking";

    @Bean
    public DirectExchange activityExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue activityQueue() {
        return new Queue(ACTIVITY_QUEUE, true);
    }

    @Bean
    public Queue analyticsQueue() {
        return new Queue(ANALYTICS_QUEUE, true);
    }

    @Bean
    public Binding bindActivityQueue() {
        return BindingBuilder.bind(activityQueue()).to(activityExchange()).with(ROUTING_KEY);
    }

    @Bean
    public Binding bindAnalyticsQueue() {
        return BindingBuilder.bind(analyticsQueue()).to(activityExchange()).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

