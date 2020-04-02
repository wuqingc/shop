package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.rabbitmq.MQSender;
import com.lele.seckill_shop.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    MQSender mqSender;

    @RequestMapping(value = "/info")
    @ResponseBody
    public Result<User> info(User user){
        return Result.success(user);
    }

    @RequestMapping(value = "/mq")
    @ResponseBody
    public Result<String> mq(){
        mqSender.send("hello,rabbit.");
        return Result.success("sucess");
    }

    @RequestMapping(value = "/mq/topic")
    @ResponseBody
    public Result<String> topic(){
        mqSender.sendTopic("hello,rabbit.");
        return Result.success("sucess");
    }

    @RequestMapping(value = "/mq/fanout")
    @ResponseBody
    public Result<String> fanout(){
        mqSender.sendFanout("hello,rabbit.");
        return Result.success("sucess");
    }

    @RequestMapping(value = "/mq/headers")
    @ResponseBody
    public Result<String> headers(){
        mqSender.sendHeader("hello,rabbit.");
        return Result.success("sucess");
    }
}
