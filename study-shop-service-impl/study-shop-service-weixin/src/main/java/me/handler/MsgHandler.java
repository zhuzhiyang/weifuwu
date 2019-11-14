package me.handler;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;
import com.zzy.constants.Constants;
import com.zzy.resps.BaseResponse;
import com.zzy.utils.RegexUtils;
import me.Feign.MemberServiceFeign;
import me.builder.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {

    // 用户发送手机验证码提示
    @Value("${zzy.weixin.registration.code.message}")
    private String registrationCodeMessage;
    // 默认用户发送验证码提示
    @Value("${zzy.weixin.default.registration.code.message}")
    private String defaultRegistrationCodeMessage;

    @Autowired
    private StringRedisTemplate redisUtils;
    @Autowired
    MemberServiceFeign memberServiceFeign;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            // TODO 可以选择将消息保存到本地
        }

        // 当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        try {
            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
                    && weixinService.getKefuService().kfOnlineList().getKfOnlineList().size() > 0) {
                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        // TODO 组装回复消息
        // 1.验证关键字是否为手机号码类型
        String fromMsg = wxMessage.getContent();

        if (RegexUtils.checkMobile(fromMsg)) {


            //调用会员服务验证手机号码是否存在
            BaseResponse<Object> result = memberServiceFeign.existMobile(fromMsg);
            if (result.getCode().equals(Constants.HTTP_RES_CODE_200)) {
                return new TextBuilder().build("手机号码"+fromMsg+"已经注册!", wxMessage, weixinService);
            }
            //返回的不是用户不存在,都直接返回
            if (!result.getCode().equals(Constants.HTTP_RES_CODE_EXISTMOBILE_203)) {
                return new TextBuilder().build(result.getMsg(), wxMessage, weixinService);
            }

            // 3.生成随机四位注册码
            int registCode = registCode();
            String content = String.format(registrationCodeMessage, registCode);
            // 4.将验证码存放在Redis中
            redisUtils.opsForValue().set(Constants.WEIXINCODE_KEY + fromMsg, registCode + "");
            redisUtils.expire(Constants.WEIXINCODE_KEY + fromMsg, Constants.WEIXINCODE_TIMEOUT, TimeUnit.SECONDS);

            return new TextBuilder().build(content, wxMessage, weixinService);
        }
        return new TextBuilder().build(defaultRegistrationCodeMessage, wxMessage, weixinService);

    }


    // 获取注册码
    private int registCode() {
        int registCode = (int) (Math.random() * 9000 + 1000);
        return registCode;
    }

}
