package com.zzy.exception;

import com.alibaba.fastjson.JSONObject;
import com.zzy.resps.BaseApiService;
import com.zzy.resps.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Sheep on 2019/11/11.
 */
@ControllerAdvice("com.zzy")
@Slf4j
public class GlobalExceptionHandler extends BaseApiService {


    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public BaseResponse<JSONObject> exceptionHandler(Exception e) {
        log.info("###全局捕获异常###,error:{}", e);
        return setResultError(e.getMessage());
    }
}
