package com.zzy.mapper;

import com.zzy.DO.UserDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Sheep on 2019/11/8.
 */
public interface UserMapper {

    @Insert("INSERT INTO `meite_user` VALUES (null,#{mobile}, #{email}, #{password}, #{userName}, null, null, null, '1', null, null, null);")
    int register(UserDO userRegInputDO);

    @Select("SELECT * FROM meite_user WHERE MOBILE=#{mobile};")
    UserDO existMobile(@Param("mobile") String mobile);


    @Select("SELECT USER_ID AS USERID ,MOBILE AS MOBILE,EMAIL AS EMAIL,PASSWORD AS PASSWORD, USER_NAME AS USER_NAME ,SEX AS SEX ,AGE AS AGE ,CREATE_TIME AS CREATETIME,IS_AVALIBLE AS ISAVALIBLE,PIC_IMG AS PICIMG,QQ_OPENID AS QQOPENID,WX_OPENID AS WXOPENID "
            + "  FROM meite_user  WHERE mobile=#{mobile} and password=#{password};")
    UserDO login(@Param("mobile") String mobile, @Param("password") String password);


    @Select("SELECT USER_ID AS USERID ,MOBILE AS MOBILE,EMAIL AS EMAIL,PASSWORD AS PASSWORD, USER_NAME AS USER_NAME ,SEX AS SEX ,AGE AS AGE ,CREATE_TIME AS CREATETIME,IS_AVALIBLE AS ISAVALIBLE,PIC_IMG AS PICIMG,QQ_OPENID AS QQOPENID,WX_OPENID AS WXOPENID"
            + " FROM meite_user WHERE user_Id=#{userId}")
    UserDO findByUserId(@Param("userId") Long userId);
}
