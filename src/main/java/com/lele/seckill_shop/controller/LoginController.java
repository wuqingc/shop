package com.lele.seckill_shop.controller;

import com.lele.seckill_shop.result.CodeMsg;
import com.lele.seckill_shop.result.Result;
import com.lele.seckill_shop.service.UserService;
import com.lele.seckill_shop.util.ValidatorUtil;
import com.lele.seckill_shop.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public Result<Boolean> doLogin(@RequestBody @Valid LoginVo loginVo){
       userService.login(loginVo);
       return Result.success(true);
    }
}
