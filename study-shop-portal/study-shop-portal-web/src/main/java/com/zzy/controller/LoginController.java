package com.zzy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.zzy.constants.Constants;
import com.zzy.dto.member.input.UserLoginInputDTO;
import com.zzy.feign.MemberServiceFeign;
import com.zzy.resps.BaseResponse;
import com.zzy.utils.ConvertorBeanUtils;
import com.zzy.utils.CookieUtils;
import com.zzy.utils.RandomValidateCodeUtil;
import com.zzy.vo.LoginVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Sheep on 2019/11/12.
 */
@Controller
public class LoginController {

    /**
     * 跳转到登陆页面页面
     */
    private static final String MB_LOGIN_FTL = "member/login";

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    /**
     * 跳转页面
     *
     * @return
     */
    @GetMapping("/login")
    public String getLogin() {
        return MB_LOGIN_FTL;
    }

    /**
     * 接受请求参数
     *
     * @return
     */
    @PostMapping("/login")
    public String postLogin(@ModelAttribute("loginVo") LoginVO loginVo, Model model, HttpServletRequest request,
                            HttpServletResponse response, HttpSession httpSession) {
        // 1.图形验证码判断
        String graphicCode = loginVo.getGraphicCode();
        if (!RandomValidateCodeUtil.checkVerify(graphicCode, httpSession)) {
            model.addAttribute("error","图形验证码不正确");
            return MB_LOGIN_FTL;
        }

        // 2.将vo转换为dto
        UserLoginInputDTO userLoginInputDTO = ConvertorBeanUtils.convert(loginVo, UserLoginInputDTO.class);
        userLoginInputDTO.setDeviceInfor("iphoneXR");
        userLoginInputDTO.setLoginType("IOS");
//        String info = webBrowserInfo(request);
        BaseResponse<JSONObject> login = memberServiceFeign.login(userLoginInputDTO);
        if (!login.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            model.addAttribute("error",login.getMsg());
            return MB_LOGIN_FTL;
        }
        // 2.将token存入到cookie中
        JSONObject data = login.getData();
        String token = data.getString("token");
        String phone  = data.getString("desensMobile");
        CookieUtils.setCookie(request, response, "token", token);
        model.addAttribute("desensMobile",  phone.substring(0, 3) + "****" + phone.substring(7));
        // 登陆成功后，跳转到首页
        return "index";
    }

    @RequestMapping("/exit")
    public String exit(HttpServletRequest request,HttpServletResponse response) {
//        // 1. 从cookie中获取token
        String token = CookieUtils.getCookieValue(request, "token");
        CookieUtils.deleteCookie(request,response,"token");
        if (!StringUtils.isEmpty(token)) {
            BaseResponse<JSONObject> delToken = memberServiceFeign.exit(token);
            if (delToken.getCode().equals(Constants.HTTP_RES_CODE_200)) {
                return "index";
            }
        }
        return "index";
    }

    /**
     * 获取浏览器信息
     *
     * @return
     */
    public String webBrowserInfo(HttpServletRequest request) {

        return "";
    }
}
