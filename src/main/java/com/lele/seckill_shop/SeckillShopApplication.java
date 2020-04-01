package com.lele.seckill_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author xuan
 * 方便于扫描类.
 */
@SpringBootApplication
public class SeckillShopApplication extends SpringBootServletInitializer{

    /**
     * war包启动方法.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SeckillShopApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SeckillShopApplication.class, args);
    }

}