package com.lele.seckill_shop.service;

import com.lele.seckill_shop.dao.GoodsDao;
import com.lele.seckill_shop.dao.OrderDao;
import com.lele.seckill_shop.domain.OrderInfo;
import com.lele.seckill_shop.domain.SeckillOrder;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.redis.OrderKey;
import com.lele.seckill_shop.redis.RedisService;
import com.lele.seckill_shop.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RedisService redisService;

    public SeckillOrder getOrderId(Long id, String goodsId) {
        return redisService.get(OrderKey.getSeckillOrder,""+id+"_"+goodsId,SeckillOrder.class);
    }


    public OrderInfo createOrder(User user, GoodsVo goodsVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderInfo.setPayDate(new Date());
        orderDao.insert(orderInfo);

        /*
         * 插入成功后,会将orderInfo数据更新
         */
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        orderDao.insertSeckillOrder(seckillOrder);

        redisService.set(OrderKey.getSeckillOrder,""+user.getId()+"_"+goodsVo.getId(),seckillOrder);

        return orderInfo;
    }

    public OrderInfo getOrderById(Long orderId) {
        return orderDao.getOrderById(orderId);
    }
}
