package cn.iocoder.boot.springsecurity.system.service.social;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.iocoder.boot.springsecurity.common.enums.CommonStatusEnum;
import cn.iocoder.boot.springsecurity.common.enums.SocialTypeEnum;
import cn.iocoder.boot.springsecurity.system.dal.DO.social.SocialClientDO;
import cn.iocoder.boot.springsecurity.system.dal.mysql.social.SocialClientMapper;
import cn.iocoder.boot.springsecurity.system.framework.justauth.core.AuthRequestFactory;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static cn.iocoder.boot.springsecurity.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.springsecurity.system.enums.ErrorCodeConstant.SOCIAL_USER_AUTH_FAILURE;
import static cn.iocoder.boot.springsecurity.system.uitl.json.JsonUtils.toJsonString;

/**
 * @author xiaosheng
 */
@Service
@Slf4j
public class SocialClientServiceImpl implements SocialClientService{

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = true) // 由于 justauth.enable 配置项，可以关闭 AuthRequestFactory 的功能，所以这里只能不强制注入
    private AuthRequestFactory authRequestFactory;

    @Resource
    private SocialClientMapper socialClientMapper;

    @Override
    public AuthUser getAuthUser(Integer socialType, Integer userType, String code, String state) {
        //构建第三方请求
        AuthRequest authRequest = buildAuthRequest(socialType, userType);
        AuthCallback authCallback = AuthCallback.builder().code(code).auth_code(code).state(state).build();

        //执行请求
        AuthResponse<AuthUser> authResponse = authRequest.login(authCallback);
        log.info("[getAuthUser][请求社交平台 type({}) request({}) response({})]", socialType,
                toJsonString(authCallback), toJsonString(authResponse));
        if (!authResponse.ok()){
            throw exception(SOCIAL_USER_AUTH_FAILURE, authResponse.getMsg());
        }
        return authResponse.getData();
    }
    /**
     * 构建 AuthRequest 对象，支持多租户配置
     *
     * @param socialType 社交类型
     * @param userType   用户类型
     * @return AuthRequest 对象
     */
    @VisibleForTesting  //实现单元测试时的public,生成自动改为private
    AuthRequest buildAuthRequest(Integer socialType,Integer userType){
        //1. 先查找默认的配置项，从 application-*.yaml 中读取
        AuthRequest request = authRequestFactory.get(SocialTypeEnum.valueOfType(socialType).getSource());
        Assert.notNull(request, String.format("社交平台(%d) 不存在", socialType));
        //2.查询 DB 的配置项，如果存在则进行覆盖(使用当前请求用户的数据,而不是配置的通用模板)
        SocialClientDO client = socialClientMapper.selectBySocialTypeAndUserType(socialType, userType);
        if(client!=null&& Objects.equals(client.getStatus(), CommonStatusEnum.ENABLE.getStatus())){
            // 2.1 构造新的 AuthConfig 对象(不影响复用)
            AuthConfig config = (AuthConfig) ReflectUtil.getFieldValue(request, "config");
            //使用反射创建实例,确保第三方类型对应
            AuthConfig newAuthConfig = ReflectUtil.newInstance(config.getClass());
            BeanUtil.copyProperties(config, newAuthConfig);

            // 2.2 修改对应的 clientId + clientSecret 密钥
            newAuthConfig.setClientId(client.getClientId());
            newAuthConfig.setClientSecret(client.getClientSecret());
//            if (client.getAgentId() != null) { // 如果有 agentId 则修改 agentId
//                newAuthConfig.setAgentId(client.getAgentId());
//            }
//            // 如果是阿里的小程序
//            if (SocialTypeEnum.ALIPAY_MINI_PROGRAM.getType().equals(socialType)) {
//                return new AuthAlipayRequest(newAuthConfig, client.getPublicKey());
//            }
//            // 2.3 设置会 request 里，进行后续使用
//            if (SocialTypeEnum.ALIPAY_MINI_PROGRAM.getType().equals(socialType)) {
//                // 特殊：如果是支付宝的小程序，多了 publicKey 属性，可见 AuthConfig 里的 alipayPublicKey 字段说明
//                return new AuthAlipayRequest(newAuthConfig, client.getPublicKey());
//            }
            ReflectUtil.setFieldValue(request, "config", newAuthConfig);
        }
        return request;
    }
}
