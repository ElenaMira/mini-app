package cn.iocoder.boot.module.member.convert;

import cn.iocoder.boot.springsecurity.member.control.app.auth.vo.AppAuthLoginRespVO;
import cn.iocoder.boot.springsecurity.member.control.app.auth.vo.AppAuthSmsLoginReqVO;
import cn.iocoder.boot.springsecurity.member.control.app.auth.vo.AppSendSmsCodeReqVO;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto.OAuth2AccessTokenBaseRespDTO;
import cn.iocoder.boot.springsecurity.system.api.sms.dto.SmsCodeSendReqDTO;
import cn.iocoder.boot.springsecurity.system.api.sms.dto.SmsCodeUseReqDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaosheng
 */
@Mapper
public interface AuthConvert {
    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

//    SocialUserBindReqDTO convert(Long userId, Integer userType, AppAuthSocialLoginReqVO reqVO);

    SmsCodeSendReqDTO convert(AppSendSmsCodeReqVO reqVO);

    SmsCodeUseReqDTO convert(AppAuthSmsLoginReqVO reqVO,Integer scene,String ip);

    AppAuthLoginRespVO convert(OAuth2AccessTokenBaseRespDTO bean, String openid);

}
