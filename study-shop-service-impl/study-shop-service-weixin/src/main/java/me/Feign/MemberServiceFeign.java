package me.Feign;

import java.lang.reflect.Member;

import com.zzy.service.MemberService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Created by Sheep on 2019/11/8.
 */
@FeignClient(value = "member-service")
public interface MemberServiceFeign extends MemberService {
}
