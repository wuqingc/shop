package com.lele.seckill_shop.redis;

/**
 * @author lele
 * 考虑枚举的可能性.
 */
public class AccessKey extends BasePrefix {
    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expireSeconds){
        return new AccessKey(expireSeconds,"as");
    }
}