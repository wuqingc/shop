package com.lele.seckill_shop.service;

import com.lele.seckill_shop.dao.GoodsDao;
import com.lele.seckill_shop.domain.OrderInfo;
import com.lele.seckill_shop.domain.SeckillOrder;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.exception.GlobalException;
import com.lele.seckill_shop.redis.RedisService;
import com.lele.seckill_shop.redis.SeckillKey;
import com.lele.seckill_shop.result.CodeMsg;
import com.lele.seckill_shop.util.UUIDUtil;
import com.lele.seckill_shop.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
public class SeckillService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo seckill(User user, GoodsVo goodsVo) {
        /*
         * 秒杀流程:
         *      1.减库存;
         *      2.下订单;
         *      3.写入秒杀订单
         *      4.返回一个订单详情对象
         */
        int res = goodsService.redeceStock(goodsVo);
        if (res == 0) {
            setGoodsOver(goodsVo.getId());
            throw new GlobalException(CodeMsg.SECKILL_OVER);
        }
        return orderService.createOrder(user,goodsVo);
    }



    public long getSeckillResult(Long id, String goodsId) {
        SeckillOrder seckillOrder = orderService.getOrderId(id,goodsId);
        if (seckillOrder != null) {
            return seckillOrder.getOrderId();
        } else {
            if (getGoodsOver(goodsId)) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    private void setGoodsOver(Long id) {
        redisService.set(SeckillKey.isGoodsOver,""+id,true);
    }
    private boolean getGoodsOver(String goodsId) {
        return redisService.exist(SeckillKey.isGoodsOver,""+goodsId);
    }

    public boolean checkPath(Long id, String goodsId, String path) {
        if (path == null) {
            return false;
        }
        String pathOld = redisService.get(SeckillKey.getSeckill,""+id+"_"+goodsId,String.class);
        return path.equals(pathOld);
    }

    public String createSeckillPath(Long id, String goodsId) {
        String str = UUIDUtil.uuid() + "123456";
        redisService.set(SeckillKey.getSeckill,""+id+"_"+goodsId,str);
        return str;
    }

    public BufferedImage createVerifyCode(User user, String goodsId) {
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SeckillKey.getSeckillVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    private int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        return ""+ num1 + op1 + num2 + op2 + num3;
    }

    public boolean checkVerifyCode(User user, String goodsId, int verifyCode) {
        Integer code = redisService.get(SeckillKey.getSeckillVerifyCode, user.getId()+","+goodsId, Integer.class);
        if (code == null || code-verifyCode != 0) {
            return false;
        }
        redisService.delete(SeckillKey.getSeckillVerifyCode, user.getId()+","+goodsId);
        return true;
    }
}
