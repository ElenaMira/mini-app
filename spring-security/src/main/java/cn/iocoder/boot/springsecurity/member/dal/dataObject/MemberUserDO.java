package cn.iocoder.boot.springsecurity.member.dal.dataObject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * @author xiaosheng
 */
@TableName(value = "member_user",autoResultMap = true)
@Data
public class MemberUserDO {
    @TableId//企业标准: 强制用包装类区分：未赋值 vs 赋值为 0
    private Long id;

    private String mobile;

    private String password;

    private Integer status;
}
