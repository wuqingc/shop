package com.lele.seckill_shop.result;

import lombok.Data;

@Data
public class CodeMsg {

    private int code;
    private String msg;
    /*
     * 通用的异常.
     * 可按照模块来进行定义异常.
     */
    public static CodeMsg SUCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500,"服务端异常");

    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500210,"密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500,"手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500,"手机号不符合规范");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500,"用户不存在");
    public static final CodeMsg PASSWORD_ERROR = new CodeMsg(500,"密码错误");


    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
