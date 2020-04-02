package com.lele.seckill_shop.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfig {

    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String ROUTING_KEY1 = "topic.key1";
    public static final String ROUTING_KEY2 = "topic.#";
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String FANOUT_QUEUE1 = "fanout.queue1";
    public static final String FANOUT_QUEUE2 = "fanout.queue2";
    public static final String HEADERS_EXCHANGE = "headersExchange";
    public static final String HEADERS_QUEUE = "headers.queue1";

    public static final String SECKILL_QUEUE = "seckill.queue1";

    /**
     * 交换机Exchange的四种模式:
     * 1.Direct模式:直接传递
     */
    @Bean
    public Queue queue() {
        return new Queue(SECKILL_QUEUE);
    }

    /**
     * Topic模式
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1);
    }
    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange())
                .with(ROUTING_KEY1);
    }
    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange())
                .with(ROUTING_KEY2);
    }

    /**
     * Fanout模式
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    @Bean
    public Queue fanoutQueue1(){
        return new Queue(FANOUT_QUEUE1,true);
    }
    @Bean
    public Queue fanoutQueue2(){
        return new Queue(FANOUT_QUEUE2,true);
    }
    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }

    /**
     * Header模式
     */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }
    @Bean
    public Queue headersQueue1(){
        return new Queue(HEADERS_QUEUE,true);
    }
    @Bean
    public Binding headerBinding() {
        Map<String,Object> map = new HashMap<>();
        map.put("header1","value1");
        map.put("header2","value2");
        return BindingBuilder.bind(headersQueue1()).to(headersExchange())
                .whereAll(map).match();
    }
}
