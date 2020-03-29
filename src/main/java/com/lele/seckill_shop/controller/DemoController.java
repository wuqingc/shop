package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.domain.User;
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

    @RequestMapping(name = "/index")
    public String index(Model model){
        model.addAttribute("name","lele");
        return "index";
    }

    @RequestMapping("/get")
    @ResponseBody
    public Result<User> index1(){
        User user = userService.getById(1);
        return Result.success(user);
    }

}
