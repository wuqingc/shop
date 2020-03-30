package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.redis.RedisService;
import com.lele.seckill_shop.result.Result;
import com.lele.seckill_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;


    @RequestMapping(name = "/index")
    public String index(Model model){
        model.addAttribute("name","lele");
        return "index";
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<String> redisGet(){
        redisService.set("key2","hello redis");
        String str = redisService.get("key2",String.class);
        return Result.success(str);
    }

}
