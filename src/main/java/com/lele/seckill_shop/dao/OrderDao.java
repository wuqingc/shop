package com.lele.seckill_shop.dao;

import com.lele.seckill_shop.domain.OrderInfo;
import com.lele.seckill_shop.domain.SeckillOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface OrderDao {

    @Select("select * from seckill_order where user_id=#{id} and goods_id=#{goodsId}")
    SeckillOrder getOrderId(Long id, String goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date,pay_date)values(" +
            "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate},#{payDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    long insert(OrderInfo orderInfo);

    @Insert("insert into seckill_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    void insertSeckillOrder(SeckillOrder seckillOrder);

    @Select("select * from order_info where id=#{orderId}")
    OrderInfo getOrderById(Long orderId);
}
