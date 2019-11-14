package com.zzy.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.zzy.DO.UserDO;
import com.zzy.DO.UserTokenDO;
import com.zzy.constants.Constants;
import com.zzy.dto.member.input.UserLoginInputDTO;
import com.zzy.dto.member.input.UserRegInputDTO;
import com.zzy.dto.member.output.UserRegOutputDTO;
import com.zzy.mapper.UserMapper;
import com.zzy.mapper.UserTokenMapper;
import com.zzy.resps.BaseApiService;
import com.zzy.resps.BaseResponse;
import com.zzy.transaction.RedisDataSourceTransaction;
import com.zzy.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Sheep on 2019/11/6.
 */
@RestController
public class MemberServiceImpl extends BaseApiService implements MemberService {


    @Autowired
    private WeixinServiceFeign weixinServiceFeign;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTokenMapper userTokenMapper;

    @Autowired
    private GenerateToken generateToken;

    @Autowired
    private RedisDataSourceTransaction manualTransaction;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public BaseResponse<Object> register(@RequestBody UserRegInputDTO userRegInputDTO, String registCode) {
        // 1.验证参数
        String userName = userRegInputDTO.getUserName();
//        if (StringUtils.isEmpty(userName)) {
//            return setResultError("用户名称不能为空!");
//        }
        String mobile = userRegInputDTO.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号码不能为空!");
        }
        String password = userRegInputDTO.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultError("密码不能为空!");
        }
        String newPassWord = MD5Util.MD5(password);
        // 将密码采用MD5加密
        userRegInputDTO.setPassword(newPassWord);
        // 调用微信接口,验证注册码是否正确
        BaseResponse<JsonObject> resultVerificaWeixinCode = weixinServiceFeign.verify(mobile,
                registCode);
        if (!resultVerificaWeixinCode.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            return setResultError(resultVerificaWeixinCode.getMsg());
        }
        int registerResult = userMapper.register(ConvertorBeanUtils.convert(userRegInputDTO, UserDO.class));
        return registerResult > 0 ? setResultSuccess("注册成功") : setResultSuccess("注册失败");
    }

    @Override
    public BaseResponse<Object> existMobile(String mobile) {
        // 1.验证参数
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号码不能为空!");
        }
        UserDO userDO = userMapper.existMobile(mobile);
        if (userDO == null) {
            return setResultError(Constants.HTTP_RES_CODE_EXISTMOBILE_203, "用户不存在");
        }
        return setResultSuccess(userDO);
    }

    @Override
    public BaseResponse<JSONObject> login(@RequestBody  UserLoginInputDTO userLoginInputDTO) {
        // 1.验证参数
        String mobile = userLoginInputDTO.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号码不能为空!");
        }
        String password = userLoginInputDTO.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultError("密码不能为空!");
        }
        // 判断登陆类型
        String loginType = userLoginInputDTO.getLoginType();
        if (StringUtils.isEmpty(loginType)) {
            return setResultError("登陆类型不能为空!");
        }
        // 目的是限制范围
        if (!(loginType.equals(Constants.MEMBER_LOGIN_TYPE_ANDROID) || loginType.equals(Constants.MEMBER_LOGIN_TYPE_IOS)
                || loginType.equals(Constants.MEMBER_LOGIN_TYPE_PC))) {
            return setResultError("登陆类型出现错误!");
        }

        // 设备信息
        String deviceInfor = userLoginInputDTO.getDeviceInfor();
        if (StringUtils.isEmpty(deviceInfor)) {
            return setResultError("设备信息不能为空!");
        }

        // 2.对登陆密码实现加密
        String newPassWord = MD5Util.MD5(password);
        // 3.使用手机号码+密码查询数据库 ，判断用户是否存在
        UserDO userDo = userMapper.login(mobile, newPassWord);
        if (userDo == null) {
            return setResultError("用户名称或者密码错误!");
        }
        TransactionStatus transactionStatus = null;
        try {

            // 1.获取用户UserId
            Long userId = userDo.getUserId();
            // 2.生成用户令牌Key
            String keyPrefix = Constants.MEMBER_TOKEN_KEYPREFIX + loginType;
            // 5.根据userId+loginType 查询当前登陆类型账号之前是否有登陆过，如果登陆过 清除之前redistoken
            UserTokenDO userTokenDo = userTokenMapper.selectByUserIdAndLoginType(userId, loginType);
            transactionStatus = manualTransaction.begin();
            // // ####开启手动事务
            if (userTokenDo != null) {
                // 如果登陆过 清除之前redistoken
                String oriToken = userTokenDo.getToken();
                // 移除Token
                generateToken.removeToken(oriToken);
                int updateTokenAvailability = userTokenMapper.updateTokenAvailability(userId, oriToken);
                if (updateTokenAvailability < 0) {
                    manualTransaction.rollback(transactionStatus);
                    return setResultError("系统错误");
                }
            }

            // 4.将用户生成的令牌插入到Token记录表中
            UserTokenDO userToken = new UserTokenDO();
            userToken.setUserId(userId);
            userToken.setLoginType(userLoginInputDTO.getLoginType());
            String newToken = generateToken.createToken(keyPrefix, userId + "");
            userToken.setToken(newToken);
            userToken.setDeviceInfor(deviceInfor);
            int result = userTokenMapper.insertUserToken(userToken);
            if (!toDaoResult(result)) {
                manualTransaction.rollback(transactionStatus);
                return setResultError("系统错误!");
            }

            // #######提交事务
            JSONObject data = new JSONObject();
            data.put("token", newToken);
            data.put("desensMobile", mobile);
            manualTransaction.commit(transactionStatus);
            return setResultSuccess(data);
        } catch (Exception e) {
            try {
                // 回滚事务
                manualTransaction.rollback(transactionStatus);
            } catch (Exception e1) {
            }
            return setResultError("系统错误!");
        }

        }

    @Override
    public BaseResponse<UserRegOutputDTO> getInfo(String token) {
        // 1.参数验证
        if (StringUtils.isEmpty(token)) {
            return setResultError("token不能为空!");
        }
        // 2.使用token向redis中查询userId
        String redisValue = generateToken.getToken(token);
        if (StringUtils.isEmpty(redisValue)) {
            return setResultError("token已经失效或者不正确");
        }
        Long userId = TypeCastHelper.toLong(redisValue);
        // 3.根据userId查询用户信息
        UserDO userDo = userMapper.findByUserId(userId);
        if (userDo == null) {
            return setResultError("用户信息不存在!");
        }
        // 4.将Do转换为Dto
        UserRegOutputDTO userRegOutputDTO = ConvertorBeanUtils.convert(userDo, UserRegOutputDTO.class);
        return setResultSuccess(userRegOutputDTO);
    }

    @Override
    public BaseResponse<JSONObject> exit(String token) {
        UserTokenDO userTokenDO = userTokenMapper.queryByToken(token);
        TransactionStatus transactionStatus = manualTransaction.begin();
        redisUtil.delKey(token);
        userTokenMapper.updateTokenAvailability(userTokenDO.getUserId(),token);
        try {
            manualTransaction.commit(transactionStatus);
        } catch (Exception e) {
            try {
                // 回滚事务
                manualTransaction.rollback(transactionStatus);
            } catch (Exception e1) {
            }
        }
        return setResultSuccess(new JSONObject());
    }


}
