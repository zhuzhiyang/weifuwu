package com.zzy.dto.member.input;

import java.util.Date;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Sheep on 2019/11/11.
 */
@Data
public class UserRegInputDTO {
    /**
     * userid
     */
    @ApiModelProperty(value = "用户id")
    private Long userid;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;
    /**
     * 性别 0 男 1女
     */
    @ApiModelProperty(value = "用户性别")
    private char sex;
    /**
     * 年龄
     */
    @ApiModelProperty(value = "用户年龄")
    private Long age;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = " 用户头像")
    private String pic_img;

}
