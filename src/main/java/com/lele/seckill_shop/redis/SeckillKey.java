package com.lele.seckill_shop.redis;

public class SeckillKey extends BasePrefix {


    public static SeckillKey isGoodsOver = new SeckillKey(0,"go");
    public static SeckillKey getSeckill = new SeckillKey(60,"gs");

    public SeckillKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
