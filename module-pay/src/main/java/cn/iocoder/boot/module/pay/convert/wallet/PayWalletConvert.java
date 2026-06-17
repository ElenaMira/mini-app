package cn.iocoder.boot.module.pay.convert.wallet;

import cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletRespVO;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaosheng
 */
@Mapper
public interface PayWalletConvert {
    PayWalletConvert INSTANCE = Mappers.getMapper(PayWalletConvert.class);

    AppPayWalletRespVO convert(PayWalletDO wallet);
}
