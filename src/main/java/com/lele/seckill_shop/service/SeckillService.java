package com.lele.seckill_shop.service;

import com.lele.seckill_shop.dao.GoodsDao;
import com.lele.seckill_shop.domain.OrderInfo;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;

    @Transactional
    public OrderInfo seckill(User user, GoodsVo goodsVo) {
        /*
         * 秒杀流程:
         *      1.减库存;
         *      2.下订单;
         *      3.写入秒杀订单
         *      4.返回一个订单详情对象
         */
        goodsService.redeceStock(goodsVo);
        return orderService.createOrder(user,goodsVo);
    }
}
