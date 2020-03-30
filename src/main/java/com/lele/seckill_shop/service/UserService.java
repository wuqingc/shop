package com.lele.seckill_shop.service;

import com.lele.seckill_shop.dao.UserDao;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.exception.GlobalException;
import com.lele.seckill_shop.redis.RedisService;
import com.lele.seckill_shop.redis.UserKey;
import com.lele.seckill_shop.result.CodeMsg;
import com.lele.seckill_shop.util.MD5Util;
import com.lele.seckill_shop.util.UUIDUtil;
import com.lele.seckill_shop.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisService redisService;

    @Transactional
    public User getById(Long id){
        return userDao.getById(id);
    }

    public boolean login(LoginVo loginVo, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(password,saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        addCookie(user,response,token);
        return true;
    }

    public User getByToken(String token, HttpServletResponse response) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.token,token,User.class);
        /*
         * 登录一次,就延长cookie的有效期
         */
        if (user != null) {
            addCookie(user,response, token);
        }
        return user;
    }

    private void addCookie(User user, HttpServletResponse response, String token){
        /*
         * 生成Cookie
         */
        redisService.set(UserKey.token,token,user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
