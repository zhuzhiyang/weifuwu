package com.zzy.service;

import com.google.gson.JsonObject;
import com.zzy.resps.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Sheep on 2019/11/6.
 */
@Api(tags = "微信服务接口")
public interface WeixinService {

    @ApiOperation("校验用户验证码是否正确")
    @PostMapping("/verify")
    BaseResponse<JsonObject> verify(@RequestParam("phone") String phone,
                                    @RequestParam("code") String code);


}
