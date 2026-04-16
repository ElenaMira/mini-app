package cn.iocoder.boot.springsecurity.system.service.sms;

import cn.iocoder.boot.springsecurity.system.dal.DO.sms.SmsTemplateDO;

/**
 * @author xiaosheng
 */
public interface SmsTemplateService {
    /**
     *   获得短信模板，从缓存中
     * @param code  模板编码
     * @return 短信模板
     */
    SmsTemplateDO getSmsTemplateByCodeFromCache(String code);
}
