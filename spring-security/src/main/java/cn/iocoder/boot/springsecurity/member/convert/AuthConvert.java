package cn.iocoder.boot.springsecurity.member.convert;

import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthSmsLoginReqVO;
import cn.iocoder.boot.springsecurity.member.control.vo.AppSendSmsCodeReqVO;
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

}
