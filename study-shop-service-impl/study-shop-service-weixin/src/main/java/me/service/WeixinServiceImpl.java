package me.service;

import com.google.gson.JsonObject;
import com.zzy.constants.Constants;
import com.zzy.resps.BaseApiService;
import com.zzy.resps.BaseResponse;
import com.zzy.service.WeixinService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Sheep on 2019/11/8.
 */
@RestController
public class WeixinServiceImpl extends BaseApiService implements WeixinService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public BaseResponse<JsonObject> verify(String phone, String code) {
        if (StringUtils.isEmpty(phone)) {
            return setResultError("手机号码不能为空!");
        }
        if (StringUtils.isEmpty(code)) {
            return setResultError("注册码不能为空!");
        }
        String registryCode = stringRedisTemplate.opsForValue().get(Constants.WEIXINCODE_KEY + phone);
        if (StringUtils.isEmpty(registryCode)) {
            return setResultError("注册码已经过期,请重新发送验证码");
        }
        if (!code.equals(registryCode)) {
            return setResultError("注册码不正确");
        }
        return setResultSuccess("注册码验证码正确");
    }
}
