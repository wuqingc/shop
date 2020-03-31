package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.domain.OrderInfo;
import com.lele.seckill_shop.domain.SeckillOrder;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.result.CodeMsg;
import com.lele.seckill_shop.service.GoodsService;
import com.lele.seckill_shop.service.OrderService;
import com.lele.seckill_shop.service.SeckillService;
import com.lele.seckill_shop.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SeckillController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;


    @RequestMapping("/doSeckill")
    public String list(User user, Model model,
                       @RequestParam("goodsId") String goodsId){

        if (user == null) {
            return "login";
        }
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() <= 0) {
            model.addAttribute("error", CodeMsg.SECKILL_OVER);
            return "seckill_fail";
        }

        SeckillOrder order = orderService.getOrderId(user.getId(),goodsId);
        if (order != null) {
            model.addAttribute("error", CodeMsg.REPEATE_SECKILL);
            return "seckill_fail";
        }


        OrderInfo orderInfo = seckillService.seckill(user,goodsVo);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goodsVo);
        return "order_detail";
    }
}
