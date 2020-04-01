package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.domain.OrderInfo;
import com.lele.seckill_shop.domain.SeckillOrder;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.result.CodeMsg;
import com.lele.seckill_shop.result.Result;
import com.lele.seckill_shop.service.GoodsService;
import com.lele.seckill_shop.service.OrderService;
import com.lele.seckill_shop.service.SeckillService;
import com.lele.seckill_shop.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SeckillController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;


    @RequestMapping(value = "/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> list(User user, Model model,
                       @RequestParam("goodsId") String goodsId){

        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        /*
         * 1.判断库存
         * 2.查看是否重复秒杀
         * 3.下订单,写入秒杀订单
         */
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() <= 0) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        SeckillOrder order = orderService.getOrderId(user.getId(),goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }

        OrderInfo orderInfo = seckillService.seckill(user,goodsVo);
        return Result.success(orderInfo);
    }
}
