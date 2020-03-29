package com.lele.seckill_shop.service;

import com.lele.seckill_shop.dao.UserDao;
import com.lele.seckill_shop.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    @Transactional
    public User getById(int id){
        return userDao.getById(id);
    }
}
