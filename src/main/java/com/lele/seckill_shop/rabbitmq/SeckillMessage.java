package com.lele.seckill_shop.rabbitmq;

import com.lele.seckill_shop.domain.User;
import lombok.Data;

@Data
public class SeckillMessage {
    private User user;
    private String goodsId;
}
