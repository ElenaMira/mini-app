package cn.iocoder.boot.springsecurity.mybatis.handle;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.boot.springsecurity.core.uitl.SecurityUtil;
import cn.iocoder.boot.springsecurity.system.dal.DO.BaseDO;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author xiaosheng
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO){
            BaseDO baseDO = (BaseDO) metaObject.getOriginalObject();

            LocalDateTime currentTime = LocalDateTimeUtil.now();
            // 创建时间为空，则以当前时间为插入时间
            if(Objects.isNull(baseDO.getCreateTime())){
                baseDO.setCreateTime(currentTime);
            }
            // 更新时间为空，则以当前时间为更新时间
            if (Objects.isNull(baseDO.getUpdateTime())) {
                baseDO.setUpdateTime(currentTime);
            }

            Long userId = SecurityUtil.getLoginUserId();
            // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
            if(Objects.nonNull(userId)&&Objects.isNull(baseDO.getCreator())){
                baseDO.setCreator(userId.toString());
            }
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            if (Objects.nonNull(userId) && Objects.isNull(baseDO.getUpdater())) {
                baseDO.setUpdater(userId.toString());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateTime = getFieldValByName("updateTime", metaObject);
        // 更新时间为空，则以当前时间为更新时间
        if (Objects.isNull(updateTime)){
            setFieldValByName("updateTime",LocalDateTimeUtil.now(),metaObject);
        }

        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        Object updater = getFieldValByName("updater", metaObject);
        Long userId = SecurityUtil.getLoginUserId();
        if (Objects.nonNull(userId) && Objects.isNull(updater)) {
            setFieldValByName("updater", userId.toString(), metaObject);
        }
    }
}
