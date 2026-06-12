package cn.iocoder.boot.module.infra.dal.mysql;

import cn.iocoder.boot.module.infra.dal.dataobject.FileConfigDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface FileConfigMapper extends BaseMapperX<FileConfigDO> {

    default FileConfigDO selectByMaster() {
        return selectOne(FileConfigDO::getMaster,true);
    }
}
