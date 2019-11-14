package com.zzy.mapper;

import com.zzy.DO.UserTokenDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Sheep on 2019/11/11.
 */
public interface UserTokenMapper {

    @Select("SELECT id as id ,token as token ,login_type as LoginType, device_infor as deviceInfor ,is_availability as isAvailability,user_id as userId FROM meite_user_token WHERE user_id=#{userId} AND login_type=#{loginType} and is_availability ='0';")
    UserTokenDO selectByUserIdAndLoginType(@Param("userId") Long userId, @Param("loginType") String loginType);

    @Update("    update meite_user_token set is_availability ='1'  where user_id=#{userId} and token =#{token} ")
    int updateTokenAvailability(@Param("userId") Long userId, @Param("token") String token);


    @Insert("    INSERT INTO `meite_user_token` VALUES (null, #{token},#{loginType}, #{deviceInfor}, 0, #{userId} ); ")
    int insertUserToken(UserTokenDO userTokenDo);


    @Select("SELECT id as id ,token as token ,login_type as LoginType, device_infor as deviceInfor ,is_availability as isAvailability,user_id as userId FROM meite_user_token WHERE token=#{token};")
    UserTokenDO queryByToken(@Param("token") String token);

}
