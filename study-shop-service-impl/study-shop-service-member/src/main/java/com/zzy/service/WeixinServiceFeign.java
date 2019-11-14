package com.zzy.service;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * Created by Sheep on 2019/11/6.
 */
@FeignClient(value = "weixin-service")
public interface WeixinServiceFeign extends WeixinService{
}
