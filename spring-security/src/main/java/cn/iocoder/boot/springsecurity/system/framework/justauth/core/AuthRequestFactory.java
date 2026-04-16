package cn.iocoder.boot.springsecurity.system.framework.justauth.core;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;

import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.request.*;

import com.xkcoding.justauth.autoconfigure.JustAuthProperties;

/**
 * @author xiaosheng
 */

@RequiredArgsConstructor
public class AuthRequestFactory {
    private final JustAuthProperties properties;
    private final AuthStateCache authStateCache;
    /**
     * 基于授权的平台枚举返回AuthRequest对象
     *
     * @param source {@link AuthSource}
     * @return {@link AuthRequest}
     */
    public AuthRequest get(String source){
        if (StrUtil.isBlank(source)) {
            throw new AuthException(AuthResponseStatus.NO_AUTH_SOURCE);
        }

        //获取配置文件默认的授权请求
        AuthRequest authRequest = getDefalutAuthRequest(source);

        if (authRequest == null) {
            throw new AuthException(AuthResponseStatus.UNSUPPORTED);
        }

        return authRequest;
    }
    private AuthRequest getDefalutAuthRequest(String source) {
        AuthDefaultSource authDefaultSource;
        try {
            authDefaultSource = EnumUtil.fromString(AuthDefaultSource.class, source.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 无自定义匹配
            return null;
        }
        AuthConfig config = properties.getType().get(authDefaultSource.name());
        if (config == null) {
            return null;
        }
//        // 配置第三方平台代理(github等) http config
//        configureHttpConfig(authDefaultSource.name(), authConfig, properties.getHttpConfig());
        switch (authDefaultSource) {
            case GITHUB:
                return new AuthGithubRequest(config, authStateCache);
            case WEIBO:
                return new AuthWeiboRequest(config, authStateCache);
            case GITEE:
                return new AuthGiteeRequest(config, authStateCache);
            case DINGTALK:
                return new AuthDingTalkRequest(config, authStateCache);
            case DINGTALK_V2:
                return new AuthDingTalkV2Request(config, authStateCache);
            case DINGTALK_ACCOUNT:
                return new AuthDingTalkAccountRequest(config, authStateCache);
            case BAIDU:
                return new AuthBaiduRequest(config, authStateCache);
            case CSDN:
                return new AuthCsdnRequest(config, authStateCache);
            case CODING:
                return new AuthCodingRequest(config, authStateCache);
            case OSCHINA:
                return new AuthOschinaRequest(config, authStateCache);
            case ALIPAY:
                return new AuthAlipayRequest(config, authStateCache);
            case QQ:
                return new AuthQqRequest(config, authStateCache);
            case WECHAT_OPEN:
                return new AuthWeChatOpenRequest(config, authStateCache);
            case WECHAT_MP:
                return new AuthWeChatMpRequest(config, authStateCache);
            case TAOBAO:
                return new AuthTaobaoRequest(config, authStateCache);
            case GOOGLE:
                return new AuthGoogleRequest(config, authStateCache);
            case FACEBOOK:
                return new AuthFacebookRequest(config, authStateCache);
            case DOUYIN:
                return new AuthDouyinRequest(config, authStateCache);
            case LINKEDIN:
                return new AuthLinkedinRequest(config, authStateCache);
            case MICROSOFT:
                return new AuthMicrosoftRequest(config, authStateCache);
            case MICROSOFT_CN:
                return new AuthMicrosoftCnRequest(config, authStateCache);

            case MI:
                return new AuthMiRequest(config, authStateCache);
            case TOUTIAO:
                return new AuthToutiaoRequest(config, authStateCache);
            case TEAMBITION:
                return new AuthTeambitionRequest(config, authStateCache);
            case RENREN:
                return new AuthRenrenRequest(config, authStateCache);
            case PINTEREST:
                return new AuthPinterestRequest(config, authStateCache);
            case STACK_OVERFLOW:
                return new AuthStackOverflowRequest(config, authStateCache);
            case HUAWEI:
                return new AuthHuaweiRequest(config, authStateCache);
            case HUAWEI_V3:
                return new AuthHuaweiV3Request(config, authStateCache);
            case WECHAT_ENTERPRISE:
                return new AuthWeChatEnterpriseQrcodeRequest(config, authStateCache);
            case WECHAT_ENTERPRISE_V2:
                return new AuthWeChatEnterpriseQrcodeV2Request(config, authStateCache);
            case WECHAT_ENTERPRISE_QRCODE_THIRD:
                return new AuthWeChatEnterpriseThirdQrcodeRequest(config, authStateCache);
            case WECHAT_ENTERPRISE_WEB:
                return new AuthWeChatEnterpriseWebRequest(config, authStateCache);
            case KUJIALE:
                return new AuthKujialeRequest(config, authStateCache);
            case GITLAB:
                return new AuthGitlabRequest(config, authStateCache);
            case MEITUAN:
                return new AuthMeituanRequest(config, authStateCache);
            case ELEME:
                return new AuthElemeRequest(config, authStateCache);
            case TWITTER:
                return new AuthTwitterRequest(config, authStateCache);
            case FEISHU:
                return new AuthFeishuRequest(config, authStateCache);
            case JD:
                return new AuthJdRequest(config, authStateCache);
            case ALIYUN:
                return new AuthAliyunRequest(config, authStateCache);
            case XMLY:
                return new AuthXmlyRequest(config, authStateCache);
            case AMAZON:
                return new AuthAmazonRequest(config, authStateCache);
            case SLACK:
                return new AuthSlackRequest(config, authStateCache);
            case LINE:
                return new AuthLineRequest(config, authStateCache);
            case OKTA:
                return new AuthOktaRequest(config, authStateCache);
            case PROGINN:
                return new AuthProginnRequest(config, authStateCache);
            case AFDIAN:
                return new AuthAfDianRequest(config, authStateCache);
            case APPLE:
                return new AuthAppleRequest(config, authStateCache);
            case FIGMA:
                return new AuthFigmaRequest(config, authStateCache);
            case WECHAT_MINI_PROGRAM:
                config.setIgnoreCheckRedirectUri(true);
                config.setIgnoreCheckState(true);
                return new AuthWechatMiniProgramRequest(config, authStateCache);
            case QQ_MINI_PROGRAM:
                config.setIgnoreCheckRedirectUri(true);
                config.setIgnoreCheckState(true);
                return new AuthQQMiniProgramRequest(config, authStateCache);
            default:
                return null;
        }
//    public void configureHttpConfig(String name,AuthConfig authConfig,JustAuthProperties.JustAuthHttpConfig httpConfig){
//        if(null==httpConfig){
//            return;
//        }
//        if()
//    }
    }
}
