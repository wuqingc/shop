package com.lele.seckill_shop.access;

import com.alibaba.fastjson.JSON;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.redis.AccessKey;
import com.lele.seckill_shop.redis.RedisService;
import com.lele.seckill_shop.result.CodeMsg;
import com.lele.seckill_shop.result.Result;
import com.lele.seckill_shop.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            User user = getUser(request,response);
            UserContext.setUser(user);

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin) {
                if (user == null) {
                    render(response,CodeMsg.SESSION_ERROR);
                    return false;
                }
                key += "_"+user.getId();
            }

            AccessKey accessKey = AccessKey.withExpire(seconds);
            Integer count = redisService.get(accessKey,key,Integer.class);
            if (count == null) {
                redisService.set(accessKey,key,1);
            } else if (count < maxCount) {
                redisService.incr(accessKey,key);
            } else {
                render(response,CodeMsg.ACCESS_LIMIT);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg codeMsg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();
        String msg = JSON.toJSONString(Result.error(codeMsg));
        outputStream.write(msg.getBytes());
        outputStream.close();
    }


    public User getUser(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(cookieToken) ? paramToken : cookieToken;
        return userService.getByToken(token,response);
    }

    private String getCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(UserService.COOKIE_NAME_TOKEN)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
