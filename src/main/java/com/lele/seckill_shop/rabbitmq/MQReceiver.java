package com.lele.seckill_shop.rabbitmq;

import com.lele.seckill_shop.domain.OrderInfo;
import com.lele.seckill_shop.domain.SeckillOrder;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.redis.RedisService;
import com.lele.seckill_shop.result.CodeMsg;
import com.lele.seckill_shop.result.Result;
import com.lele.seckill_shop.service.GoodsService;
import com.lele.seckill_shop.service.OrderService;
import com.lele.seckill_shop.service.SeckillService;
import com.lele.seckill_shop.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

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


    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void seckill(String message) {
        SeckillMessage seckillMessage = RedisService.StringToBean(message,SeckillMessage.class);
        User user = seckillMessage.getUser();
        String goodId = seckillMessage.getGoodsId();

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodId);
        if (goodsVo.getStockCount() <= 0) {
            return;
        }
        SeckillOrder order = orderService.getOrderId(user.getId(),goodId);
        if (order != null) {
            return;
        }
        seckillService.seckill(user,goodsVo);
    }
}
