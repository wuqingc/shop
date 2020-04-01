package com.lele.seckill_shop.dao;

import com.lele.seckill_shop.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    User getById(Long id);

    @Insert("insert into user(id,password,salt) values(#{id},#{password},#{salt})")
    Boolean insert(User user);

    @Update("update user set password=#{password} where id = #{id}")
    void update(User updateUser);
}
