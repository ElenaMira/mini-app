package cn.iocoder.boot.module.trade.dal.mysql.cart;

import cn.iocoder.boot.module.trade.dal.dataobject.cart.CartDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface CartMapper extends BaseMapperX<CartDO> {

    default List<CartDO> selectListByUserId(Long loginUserId) {
        return selectList(CartDO::getUserId, loginUserId);
    }
}
