package com.lele.seckill_shop.redis;

public class SeckillKey extends BasePrefix {
    public SeckillKey(String prefix) {
        super(prefix);
    }
    public static SeckillKey isGoodsOver = new SeckillKey("go");
}
