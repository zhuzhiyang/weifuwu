package com.zzy.service;


import com.alibaba.fastjson.JSONObject;
import com.zzy.dto.member.input.UserLoginInputDTO;
import com.zzy.dto.member.input.UserRegInputDTO;
import com.zzy.dto.member.output.UserRegOutputDTO;
import com.zzy.resps.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Sheep on 2019/11/6.
 */
@Api(tags = "会员服务接口")
public interface MemberService {


    @PostMapping("/register")
    @ApiOperation(value = "会员用户注册信息接口")
    BaseResponse register(@RequestBody UserRegInputDTO userRegInputDTO,
                                      @RequestParam("registCode") String registCode);


    /**
     * 根据手机号码查询是否已经存在,如果存在返回当前用户信息
     *
     * @param mobile
     * @return
     */
    @ApiOperation(value = "根据手机号码查询是否已经存在")
    @PostMapping("/existMobile")
    BaseResponse<Object> existMobile(@RequestParam("mobile") String mobile);



    /**
     * 用户登陆接口
     *
     * @param userLoginInputDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "会员用户登陆信息接口")
    BaseResponse<JSONObject> login(@RequestBody UserLoginInputDTO userLoginInputDTO);

    /**
     * 根据token查询用户信息
     *
     * @param
     * @return
     */
    @GetMapping("/getUserInfo")
    @ApiOperation(value = "/getUserInfo")
    BaseResponse<UserRegOutputDTO> getInfo(@RequestParam("token") String token);


    /**
     * 用户登陆接口
     *
     * @param token
     * @return
     */
    @PostMapping("/exit")
    @ApiOperation(value = "注销登陆")
    BaseResponse<JSONObject> exit(@RequestParam String token);

}
