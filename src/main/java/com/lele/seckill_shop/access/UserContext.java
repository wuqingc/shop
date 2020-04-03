package com.lele.seckill_shop.access;

import com.lele.seckill_shop.domain.User;

public class UserContext {
    /*
     * ThreadLocal是与当前线程绑定,每个线程单独存一份.
     */
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setUser(User user){
        userThreadLocal.set(user);
    }
    public static User getUser(){
        return userThreadLocal.get();
    }
}
