package com.lele.seckill_shop.redis;

public class GoodsKey extends BasePrefix {

    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
    public static GoodsKey getDetail = new GoodsKey(60,"dl");

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
