package com.lele.seckill_shop;

import com.lele.seckill_shop.util.ValidatorUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class SeckillShopApplicationTests {

    @Test
    void test() {
        System.out.println(isValid("()"));
    }

    public boolean isValid(String s) {
        while(s.contains("()")||s.contains("[]")||s.contains("{}")){
            s = s.replaceAll("\\(\\)","");
            System.out.println(s);
        }
        return s=="";
    }
}
