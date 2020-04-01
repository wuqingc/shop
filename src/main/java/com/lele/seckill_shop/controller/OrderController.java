package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.domain.OrderInfo;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.redis.RedisService;
import com.lele.seckill_shop.result.CodeMsg;
import com.lele.seckill_shop.result.Result;
import com.lele.seckill_shop.service.GoodsService;
import com.lele.seckill_shop.service.OrderService;
import com.lele.seckill_shop.service.UserService;
import com.lele.seckill_shop.vo.GoodsVo;
import com.lele.seckill_shop.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/orderDetail")
    @ResponseBody

    public Result<OrderDetailVo> info(Model model, User user,
                                      @RequestParam("orderId") Long orderId) {
        if(user == null) {
            return Result.error(CodeMsg.MOBILE_EXIST);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if(order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        String goodsId = order.getGoodsId().toString();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrderInfo(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }
}
