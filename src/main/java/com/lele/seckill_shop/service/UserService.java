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

    /**
     * 不要调用别人的Dao,因为Service中可能有缓存.
     * 尽量做到Service调用.
     */
    @Transactional
    public User getById(Long id){
        User user = redisService.get(UserKey.getById,""+id,User.class);
        if (user != null) {
            return user;
        }
        user = userDao.getById(id);
        if (user != null) {
            redisService.set(UserKey.getById,""+id,user);
        }
        return user;
    }

    @Transactional
    public boolean updatePassword(String token,Long id,String formPass){
        User user = getById(id);
        if (user != null) {
           throw new GlobalException(CodeMsg.MOBILE_EXIST);
        }
        /*
         * 需要修改的字段才设置.
         */
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setPassword(MD5Util.formPassToDBPass(formPass,user.getSalt()));
        userDao.update(updateUser);

        /*
         * 先更新数据库后,更新缓存.
         */
        redisService.delete(UserKey.getById,""+id);
        user.setPassword(updateUser.getPassword());
        redisService.set(UserKey.token,token,user);
        return true;
    }

    public String login(LoginVo loginVo, HttpServletResponse response) {
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
        return token;
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

    public Boolean insert(LoginVo loginVo) {
        User user = new User();
        user.setId(Long.valueOf(loginVo.getMobile()));
        String pwd = MD5Util.formPassToDBPass(loginVo.getPassword(),"1a2b3c");
        user.setPassword(pwd);
        user.setSalt("1a2b3c");
        return userDao.insert(user);
    }
}
