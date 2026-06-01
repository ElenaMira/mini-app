package cn.iocoder.boot.ip.core.utils;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.iocoder.boot.common.util.object.ObjectUtils;
import cn.iocoder.boot.ip.core.Area;
import cn.iocoder.boot.ip.core.enums.AreaTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaosheng
 */
@Slf4j
public class AreaUtils {
    /**
     * Area 内存缓存，提升访问速度
     */
    private static Map<Integer, Area> areas;

    static {
        init();
    }

    /**
     * 初始化csv文件的地址,构建缓存地址树便于format递归
     */
    private static void init(){
        try{
            long now =System.currentTimeMillis();
            areas = new HashMap<>();
            areas.put(Area.ID_GLOBAL,Area.builder()
                            .id(Area.ID_GLOBAL)
                            .name("全球")
                            .type(0)
                            .parent(null)
                            .children(new ArrayList<>())
                    .build());
            List<CsvRow> rows = CsvUtil.getReader().read(ResourceUtil.getUtf8Reader("area.csv")).getRows();
            //移除第一行(第一行为属性行)
            rows.remove(0);
            for (CsvRow row:rows){
                Area area = Area.builder().id(Integer.valueOf(row.get(0)))
                        .name(row.get(1))
                        .type(Integer.valueOf(row.get(2)))
                        .parent(null)
                        .children(new ArrayList<>())
                        .build();
                areas.put(area.getId(),area);
            }
            // 构建父子关系：因为 Area 中没有 parentId 字段,所以需要重复读取
            for (CsvRow row : rows){
                Area area = areas.get(Integer.valueOf(row.get(0)));
                Area parent = areas.get(Integer.valueOf(row.get(3)));
                Assert.isTrue(area!=parent,"{}:父子节点相同",area.getName());
                area.setParent(parent);
                parent.getChildren().add(area);
            }
            log.info("启动加载 AreaUtils 成功，耗时 ({}) 毫秒", System.currentTimeMillis() - now);
        }catch (Exception e) {
            throw new RuntimeException("AreaUtils 初始化失败", e);
        }
    }
    /**
     * 获得指定编号对应的区域
     *
     * @param id 区域编号
     * @return 区域
     */
    public static Area getArea(Integer id) {
        return areas.get(id);
    }
    /**
     *
     * @param id
     * @return
     */
    public static String format(Integer id){
        return format(id," ");
    }
    /**
     * 格式化区域,获取本身地址以及所有父类地区
     *
     * 例如说：
     * 1. id = “静安区”时：上海 上海市 静安区
     * 2. id = “上海市”时：上海 上海市
     * 3. id = “上海”时：上海
     * 4. id = “美国”时：美国
     * 当区域在中国|国外时，默认不显示中国|国外
     *
     * @param id        区域编号
     * @param separator 分隔符
     * @return 格式化后的区域
     */
    public static String format(Integer id, String separator){
        Area area = areas.get(id);
        if (area == null){
            return null;
        }

        // 格式化
        StringBuilder sb = new StringBuilder();
        // 只循环枚举长度,避免死循环
        for (int i=0;i< AreaTypeEnum.values().length;i++){
            sb.insert(0,area.getName());
            //递归"父节点"
            area = area.getParent();
            // 跳过父节点为中国或全球的情况
            if (area == null
                    || ObjectUtils.equalsAny(area.getId(), Area.ID_GLOBAL, Area.ID_CHINA)) {
                break;
            }
            sb.insert(0, separator);
        }
        // 最终获得""
        return sb.toString();
    }
}
