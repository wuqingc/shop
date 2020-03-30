package com.lele.seckill_shop.redis;

public class UserKey extends BasePrefix{

    public static final int TOKEN_EXPIRE = 3600*24*2;

    private UserKey(int expire,String prefix) {
        super(expire,prefix);
    }

    public static UserKey token = new UserKey(TOKEN_EXPIRE,"token");
}
