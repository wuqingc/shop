package com.lele.seckill_shop.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    public static Logger logger = LoggerFactory.getLogger(MQReceiver.class);


    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        logger.info("receive message:" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void topic1(String message) {
        logger.info("receive topic queue1 message:" + message);
    }
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void topic2(String message) {
        logger.info("receive topic queue2 message:" + message);
    }

    @RabbitListener(queues = MQConfig.FANOUT_QUEUE1)
    public void fanout1(String message) {
        logger.info("receive fanout queue1 message:" + message);
    }
    @RabbitListener(queues = MQConfig.FANOUT_QUEUE2)
    public void fanout2(String message) {
        logger.info("receive fanout queue2 message:" + message);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void headers(byte[] message) {
        logger.info("receive headers queue2 message:" + new String(message));
    }
}
