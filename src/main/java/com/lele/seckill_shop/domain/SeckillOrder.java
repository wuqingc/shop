package com.lele.seckill_shop.domain;

import lombok.Data;

@Data
public class SeckillOrder {
	private Long id;
	private Long userId;
	private Long  orderId;
	private Long goodsId;
}
