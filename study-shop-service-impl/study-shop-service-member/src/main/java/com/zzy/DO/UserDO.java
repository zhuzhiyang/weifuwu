package com.zzy.DO;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Sheep on 2019/11/11.
 */
@Data
public class UserDO {

    private Long userId;
    
    private String password;
    
    private String mobile;

    private String email;

    private String userName;

    private char sex;

    private Long age;

    private Date createTime;

    private Date updateTime;

    private char is_avalible;

    private String pic_img;

    private Date qq_openid;

    private Date WX_OPENID;
}
