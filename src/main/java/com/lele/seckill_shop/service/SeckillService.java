package com.lele.seckill_shop.service;

import com.lele.seckill_shop.dao.GoodsDao;
import com.lele.seckill_shop.domain.OrderInfo;
import com.lele.seckill_shop.domain.SeckillOrder;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.exception.GlobalException;
import com.lele.seckill_shop.redis.RedisService;
import com.lele.seckill_shop.redis.SeckillKey;
import com.lele.seckill_shop.result.CodeMsg;
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
    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo seckill(User user, GoodsVo goodsVo) {
        /*
         * 秒杀流程:
         *      1.减库存;
         *      2.下订单;
         *      3.写入秒杀订单
         *      4.返回一个订单详情对象
         */
        int res = goodsService.redeceStock(goodsVo);
        if (res == 0) {
            setGoodsOver(goodsVo.getId());
            throw new GlobalException(CodeMsg.SECKILL_OVER);
        }
        return orderService.createOrder(user,goodsVo);
    }



    public long getSeckillResult(Long id, String goodsId) {
        SeckillOrder seckillOrder = orderService.getOrderId(id,goodsId);
        if (seckillOrder != null) {
            return seckillOrder.getOrderId();
        } else {
            if (getGoodsOver(goodsId)) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    private void setGoodsOver(Long id) {
        redisService.set(SeckillKey.isGoodsOver,""+id,true);
    }
    private boolean getGoodsOver(String goodsId) {
        return redisService.exist(SeckillKey.isGoodsOver,""+goodsId);
    }
}
