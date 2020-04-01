package com.lele.seckill_shop.vo;

import com.lele.seckill_shop.domain.User;
import lombok.Data;

@Data
public class GoodsDetailVo {
    private int seckillStatus = 0;
    private long remainSeconds = 0;
    private GoodsVo goods ;
    private User user;
}
