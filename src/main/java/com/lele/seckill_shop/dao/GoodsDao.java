package com.lele.seckill_shop.dao;

import com.lele.seckill_shop.domain.Goods;
import com.lele.seckill_shop.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("select goods.*, seckill_goods.stock_count, seckill_goods.start_date,seckill_goods.end_date,seckill_goods.seckill_price from seckill_goods left join goods on seckill_goods.goods_id = goods.id")
    List<GoodsVo> list();

    @Select("select goods.*, seckill_goods.stock_count, seckill_goods.start_date,seckill_goods.end_date,seckill_goods.seckill_price from seckill_goods left join goods on seckill_goods.goods_id = goods.id where goods.id = #{goodsId}")
    GoodsVo getGood(String goodsId);
}
