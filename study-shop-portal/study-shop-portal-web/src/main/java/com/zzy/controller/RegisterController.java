package com.zzy.controller;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.zzy.constants.Constants;
import com.zzy.dto.member.input.UserRegInputDTO;
import com.zzy.feign.MemberServiceFeign;
import com.zzy.resps.BaseResponse;
import com.zzy.utils.ConvertorBeanUtils;
import com.zzy.vo.RegisterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by Sheep on 2019/11/12.
 */
@Controller
public class RegisterController {

    private static final String MB_REGISTER_FTL = "member/register";
    private static final String MB_LOGIN_PAGE = "member/login";


    @Autowired
    private MemberServiceFeign memberServiceFeign;

    /**
     * 跳转到注册页面
     *
     * @return
     */
    @GetMapping("/register")
    public String getRegister() {
        return MB_REGISTER_FTL;
    }

    /**
     * 跳转到注册页面
     *
     * @return
     */
    @PostMapping("/register")
    public String postRegister(@ModelAttribute("registerVo") @Validated RegisterVO registerVo,
                               BindingResult bindingResult, HttpSession httpSession, Model model) {
        // 1.参数验证
        if (bindingResult.hasErrors()) {
            // 获取第一个错误!
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            model.addAttribute("error",errorMsg);
            return MB_REGISTER_FTL;
        }

        // 将VO转换DTO
        UserRegInputDTO userRegInputDTO = ConvertorBeanUtils.convert(registerVo, UserRegInputDTO.class);
        try {
            String registCode = registerVo.getRegistCode();
            BaseResponse<Object> register = memberServiceFeign.register(userRegInputDTO, registCode);
            if (!register.getCode().equals(Constants.HTTP_RES_CODE_200)) {
                model.addAttribute("error", register.getMsg());
                return MB_REGISTER_FTL;
            }
        } catch (Exception e) {
            model.addAttribute("error", "系统出现错误!");
            return MB_REGISTER_FTL;
        }

        // 注册成功,跳转到登陆页面
        return MB_LOGIN_PAGE;
    }

}
