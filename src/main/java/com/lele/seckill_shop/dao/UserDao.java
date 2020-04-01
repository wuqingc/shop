package com.lele.seckill_shop.dao;

import com.lele.seckill_shop.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    User getById(Long id);

    @Insert("insert into user(id,password,salt) values(#{id},#{password},#{salt})")
    Boolean insert(User user);
}
