package com.lele.seckill_shop.result;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(){}

    /**
     * 正确的状态只有一种,错误有多种.
     * @param codeMsg
     */
    private Result(CodeMsg codeMsg) {
        if (codeMsg == null) {
            return;
        }
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }

    public static <T> Result<T> success(T data){
        return new Result<>(data);

    }
    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<>(codeMsg);
    }
}
