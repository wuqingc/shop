package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.domain.User;

import com.lele.seckill_shop.service.GoodsService;
import com.lele.seckill_shop.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value = "/goodsDetail/{goodsId}")
    public String toList(Model model, User user,
                         @PathVariable(name = "goodsId") String goodsId){
        model.addAttribute("user",user);

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goodsVo);

        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 1;
        long remainSeconds = 0;

        if (now < startAt) {
            seckillStatus = 0;
            remainSeconds = (startAt - now) / 1000;
        } else if (now > endAt) {
            seckillStatus = 2;
            remainSeconds = -1;
        }

        model.addAttribute("seckillStatus",seckillStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        return "goods_detail";
    }
}
