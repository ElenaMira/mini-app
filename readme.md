# 
## 规范
### 软件包
1. 小驼峰
2. 特殊称呼全部小写(如: vo do,mysql等)
#### 代码
##### builder
```示例
        smsCodeApi.useSmsCode(SmsCodeUseReqDTO.builder()
                .mobile(userDO.getMobile())
                .scene(SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene())
                .code(reqVO.getCode())
                .usedIp(getClientIP()).build());
```
### 上传
1. 方法: 新增,删除,修改,