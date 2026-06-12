package cn.iocoder.boot.module.infra.dal.mysql;

import cn.iocoder.boot.module.infra.dal.dataobject.FileDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface FileMapper extends BaseMapperX<FileDO> {

}
