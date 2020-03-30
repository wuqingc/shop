package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.domain.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GoodsController {

    @RequestMapping(value = "/toList")
    public String toList(Model model,User user){
        model.addAttribute("user",user);
        return "goods_list";
    }
}
