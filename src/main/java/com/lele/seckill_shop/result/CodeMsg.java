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

    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
