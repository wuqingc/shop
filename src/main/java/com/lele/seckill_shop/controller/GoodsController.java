package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.domain.User;

import com.lele.seckill_shop.redis.GoodsKey;
import com.lele.seckill_shop.redis.RedisService;
import com.lele.seckill_shop.result.Result;
import com.lele.seckill_shop.service.GoodsService;
import com.lele.seckill_shop.vo.GoodsDetailVo;
import com.lele.seckill_shop.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/toList",produces = "text/html")
    @ResponseBody
    public String toList(Model model, User user,
                         HttpServletResponse response,
                         HttpServletRequest request){
        model.addAttribute("user",user);
        /*
         * 取缓存
         */
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        /*
         * 手动渲染模板.
         */
        List<GoodsVo> list = goodsService.listGoods();
        model.addAttribute("goodsList",list);
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

    @RequestMapping(value = "/goodsDetail/{goodsId}",produces = "text/html")
    @ResponseBody
    public String detail(Model model, User user,
                         @PathVariable(name = "goodsId") String goodsId,
                         HttpServletResponse response,
                         HttpServletRequest request){
        model.addAttribute("user",user);

        /*
         * 取缓存
         */
        String html = redisService.get(GoodsKey.getGoodsList,""+goodsId,String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

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

        /*
         * 手动渲染模板.
         */
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList,""+goodsId,html);
        }
        return html;
    }


    /**
     * 页面静态化方式.
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/goodsDetail1/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail2(Model model, User user,
                                         @PathVariable(name = "goodsId") String goodsId){

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
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
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goodsVo);
        goodsDetailVo.setUser(user);
        goodsDetailVo.setSeckillStatus(seckillStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);

        return Result.success(goodsDetailVo);
    }
}
