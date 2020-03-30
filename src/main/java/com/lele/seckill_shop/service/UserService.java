package com.lele.seckill_shop.service;

import com.lele.seckill_shop.dao.UserDao;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.result.CodeMsg;
import com.lele.seckill_shop.util.MD5Util;
import com.lele.seckill_shop.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    @Transactional
    public User getById(Long id){
        return userDao.getById(id);
    }

    public CodeMsg login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            return CodeMsg.MOBILE_NOT_EXIST;
        }

        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(password,saltDB);
        if (!calcPass.equals(dbPass)) {
            return CodeMsg.PASSWORD_ERROR;
        }
        return CodeMsg.SUCESS;
    }
}
