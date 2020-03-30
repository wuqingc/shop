package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.domain.User;

import com.lele.seckill_shop.service.GoodsService;
import com.lele.seckill_shop.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/toList")
    public String toList(Model model,User user){
        model.addAttribute("user",user);

        List<GoodsVo> list = goodsService.listGoods();
        model.addAttribute("goodsList",list);
        return "goods_list";
    }
}
