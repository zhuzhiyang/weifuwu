package com.zzy.dto.member.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Sheep on 2019/11/11.
 */
@Data
public class UserLoginInputDTO {

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 登陆类型 PC、Android 、IOS
     */
    @ApiModelProperty(value = "登陆类型")
    private String loginType;
    /**
     * 设备信息
     */
    @ApiModelProperty(value = "设备信息")
    private String deviceInfor;
}
