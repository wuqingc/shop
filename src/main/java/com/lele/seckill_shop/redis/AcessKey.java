package com.lele.seckill_shop.redis;

public class AcessKey extends BasePrefix {
    public AcessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static AcessKey acessKey = new AcessKey(5,"as");
}
