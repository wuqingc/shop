package com.lele.seckill_shop.vo;

import com.lele.seckill_shop.domain.Goods;
import com.lele.seckill_shop.domain.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVo {
    private OrderInfo orderInfo;
    private Goods goods;
}
