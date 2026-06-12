package cn.iocoder.boot.module.infra.service.file;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.boot.module.infra.dal.dataobject.FileConfigDO;
import cn.iocoder.boot.module.infra.dal.mysql.FileConfigMapper;
import cn.iocoder.boot.module.infra.framework.file.core.client.FileClient;
import cn.iocoder.boot.module.infra.framework.file.core.client.FileClientFactory;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

import static cn.iocoder.boot.common.util.cache.CacheUtils.buildAsyncReloadingCache;

/**
 * @author xiaosheng
 */
@Service
public class FileConfigServiceImpl implements FileConfigService {
    @Resource
    private FileConfigMapper fileConfigMapper;
    @Resource
    private FileClientFactory fileClientFactory;


    /**
     *  CACHE_MASTER_ID默认的缓存加载ID(兜底非法ID)
     */
    private static final Long CACHE_MASTER_ID = 0L;
    @Getter
    private final LoadingCache<String,FileClient> cache = buildAsyncReloadingCache(Duration.ofSeconds(10L),
        new CacheLoader<Long,FileClient>() {
            @Override
            public FileClient load(Long id) {
                FileConfigDO config = ObjectUtil.equals(id, CACHE_MASTER_ID) ?
                        fileConfigMapper.selectByMaster() : fileConfigMapper.selectById(id);
                if(config != null) {
                    fileClientFactory.createOrUpdateFileClient(config.getId(),config.getStorage(),config.getC)
                }
            }
    });


    @Override
    public FileClient getMasterFileClient() {
        return clientCache.getUnchecked(CACHE_MASTER_ID);
    }
}
