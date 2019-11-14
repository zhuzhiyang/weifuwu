package com.zzy.vo;

import javax.validation.Valid;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Sheep on 2019/11/12.
 */
@Data
@Valid
public class LoginVO {

    private String mobile;

    private String password;

    private String loginType;

    private String deviceInfor;

    private String graphicCode;
}
