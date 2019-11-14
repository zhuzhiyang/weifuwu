package com.zzy.feign;

import java.lang.reflect.Member;

import com.zzy.service.MemberService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Created by Sheep on 2019/11/12.
 */
@FeignClient(name = "member-service")
public interface MemberServiceFeign extends MemberService {
}
