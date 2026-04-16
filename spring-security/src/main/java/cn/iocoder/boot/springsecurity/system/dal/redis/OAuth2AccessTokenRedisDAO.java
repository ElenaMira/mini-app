package cn.iocoder.boot.springsecurity.system.dal.redis;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth.OAuth2AccessTokenDO;
import cn.iocoder.boot.springsecurity.system.uitl.json.JsonUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaosheng
 * {@link OAuth2AccessTokenDO}
 */
@Repository
public class OAuth2AccessTokenRedisDAO {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    public OAuth2AccessTokenDO get(String accessToken) {
        // 检测accessToken是否合法
        String formatKey = formatKey(accessToken);
        return JsonUtils.parseObject(stringRedisTemplate.opsForValue().get(formatKey), OAuth2AccessTokenDO.class);
    }
    public void set(OAuth2AccessTokenDO accessTokenDO){
        String formatKey = formatKey(accessTokenDO.getAccessToken());
        //清空字段,只存有用的数据在redis中
        accessTokenDO.setUpdater(null).setUpdateTime(null).setCreateTime(null).setCreator(null).setDeleted(null);
        long time = LocalDateTimeUtil.between(LocalDateTime.now(), accessTokenDO.getExpiresTime(), ChronoUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(formatKey,JsonUtils.toJsonString(accessTokenDO),time, TimeUnit.SECONDS);
    }
    public static String formatKey(String accessToken){
        return String.format("oauth2_access_token:%s", accessToken);
    }
}
