package com.zzy.DO;

import java.util.Date;

import lombok.Data;

/**
 * Created by Sheep on 2019/11/11.
 */
@Data
public class UserTokenDO {

    /**
     * id
     */
    private Long id;
    /**
     * 用户token
     */
    private String token;
    /**
     * 登陆类型
     */
    private String loginType;

    /**
     * 设备信息
     */
    private String deviceInfor;
    /**
     * 用户userId
     */
    private Long userId;

    /**
     * 注册时间
     */
    private Date createTime;
    /**
     * 修改时间
     *
     */
    private Date updateTime;

    /**
     * 是否可用 0可用 1不可用
     */
    private Long isAvailability;
}
