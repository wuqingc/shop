package com.lele.seckill_shop.redis;

public interface KeyPrefix {
    /**
     * 默认0代表永不过期.
     * @return 过期时间
     */
    int expireSeconds();
    String getPrefix();
}
