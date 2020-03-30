package com.lele.seckill_shop.exception;

import com.lele.seckill_shop.result.CodeMsg;
import lombok.Data;

@Data
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

}
