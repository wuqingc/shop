package com.lele.seckill_shop.exception;

import com.lele.seckill_shop.result.CodeMsg;
import com.lele.seckill_shop.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request,
                                           Exception e){

        if (e instanceof GlobalException) {
            return Result.error(((GlobalException) e).getCodeMsg());
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = ex.getBindingResult();

            StringBuilder errorMesssage = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMesssage.append(fieldError.getDefaultMessage()).append("\n");
            }
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(errorMesssage.toString()));
        } else {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
