package com.lele.seckill_shop.redis;

public class OrderKey extends BasePrefix {
    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getSeckillOrder = new OrderKey("sec");


}
