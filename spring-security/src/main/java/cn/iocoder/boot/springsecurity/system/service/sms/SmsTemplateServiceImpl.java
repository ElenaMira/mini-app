package cn.iocoder.boot.springsecurity.system.service.sms;

import cn.iocoder.boot.springsecurity.system.dal.DO.sms.SmsTemplateDO;
import cn.iocoder.boot.springsecurity.system.dal.mysql.sms.SmsTemplateMapper;
import cn.iocoder.boot.springsecurity.system.dal.redis.RedisKeyConstants;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class SmsTemplateServiceImpl implements SmsTemplateService{
    @Resource
    private SmsTemplateMapper smsTemplateMapper;
    @Override
    @Cacheable(cacheNames = RedisKeyConstants.SMS_TEMPLATE,key = "#code",
        unless = "#result == null")
    public SmsTemplateDO getSmsTemplateByCodeFromCache(String code) {
        return smsTemplateMapper.selectByCode(code);
    }
}
