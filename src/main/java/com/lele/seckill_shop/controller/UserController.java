package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @RequestMapping(value = "/info")
    @ResponseBody
    public Result<User> info(User user){
        return Result.success(user);
    }
}
