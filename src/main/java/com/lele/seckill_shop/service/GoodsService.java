package com.lele.seckill_shop.service;

import com.lele.seckill_shop.dao.GoodsDao;
import com.lele.seckill_shop.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoods(){
        return goodsDao.list();
    }

    public GoodsVo getGoodsVoByGoodsId(String goodsId) {
        return goodsDao.getGood(goodsId);
    }
}
