package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.domain.OrderInfo;
import com.lele.seckill_shop.domain.SeckillOrder;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.rabbitmq.MQSender;
import com.lele.seckill_shop.rabbitmq.SeckillMessage;
import com.lele.seckill_shop.redis.GoodsKey;
import com.lele.seckill_shop.redis.RedisService;
import com.lele.seckill_shop.result.CodeMsg;
import com.lele.seckill_shop.result.Result;
import com.lele.seckill_shop.service.GoodsService;
import com.lele.seckill_shop.service.OrderService;
import com.lele.seckill_shop.service.SeckillService;
import com.lele.seckill_shop.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SeckillController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender mqSender;

    private Map<Long,Boolean> localOver = new HashMap<>();


    @RequestMapping(value = "/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(User user, Model model,
                       @RequestParam("goodsId") String goodsId){

        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        /*
         * 1.判断库存
         * 2.查看是否重复秒杀
         * 3.下订单,写入秒杀订单
         */
        /*
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
        */

        /*
         * 利用内存标记来减少redis的访问次数.
         */
        boolean over = localOver.get(Long.valueOf(goodsId));
        if (over) {
            Result.error(CodeMsg.SECKILL_OVER);
        }

        long stock = redisService.decr(GoodsKey.getSeckillGood,""+goodsId);
        if (stock <= 0) {
            localOver.put(Long.valueOf(goodsId),true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        SeckillOrder order = orderService.getOrderId(user.getId(),goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setGoodsId(goodsId);
        seckillMessage.setUser(user);
        mqSender.seckillMessage(seckillMessage);
        return Result.success(0);
    }



    /**
     * 系统初始化时,将商品加载到缓存中去.
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoods();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo good : goodsList) {
            redisService.set(GoodsKey.getSeckillGood,""+good.getId(),good.getStockCount());
            localOver.put(good.getId(),false);
        }
    }

    /**
     * 秒杀成功,返回OrderId
     * 秒杀失败.返回-1
     * 排队中,0
     * @param user
     * @param model
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/seckillResult",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> result(User user, Model model,
                                @RequestParam("goodsId") String goodsId){
        model.addAttribute("user",user);
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        long result = seckillService.getSeckillResult(user.getId(),goodsId);
        return Result.success(result);
    }
}
