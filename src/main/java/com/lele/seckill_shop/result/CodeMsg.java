package com.lele.seckill_shop.result;

import lombok.Data;

@Data
public class CodeMsg {


    public static final CodeMsg ORDER_NOT_EXIST = new CodeMsg(500,"订单不存在");
    private int code;
    private String msg;
    /*
     * 通用的异常.
     * 可按照模块来进行定义异常.
     */
    public static CodeMsg SUCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(508,"服务端异常");
    public static final CodeMsg BIND_ERROR = new CodeMsg(500,"参数校验异常:");

    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500210,"密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500,"手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500,"手机号不符合规范");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(501,"用户不存在");
    public static final CodeMsg PASSWORD_ERROR = new CodeMsg(500,"密码错误");
    public static final CodeMsg SECKILL_OVER = new CodeMsg(502,"商品秒杀结束");
    public static final CodeMsg REPEATE_SECKILL = new CodeMsg(503,"不能重复秒杀");

    public static final CodeMsg MOBILE_EXIST = new CodeMsg(500,"手机号码不存在");
    public static final CodeMsg REQUEST_ILLEGAL = new CodeMsg(500,"请求非法.");


    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(String error) {
        String message = this.msg + error;
        return new CodeMsg(this.code,message);
    }
}
