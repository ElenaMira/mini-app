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
    @TableId
    private long id;

    private String mobile;

    private String password;

    private Integer status;
}
