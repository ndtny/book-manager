package com.david.bookmanager.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User {

    /**
     * 用户ID，主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名，唯一
     */
    @TableField("username")
    private String username;

    /**
     * 密码，加密存储
     */
    @TableField("password")
    private String password;

    /**
     * 邮箱地址
     */
    @TableField("email")
    private String email;

    /**
     * 手机号码
     */
    @TableField("phone")
    private String phone;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 用户角色，admin或user
     */
    @TableField("role")
    private String role;

    /**
     * 用户状态，1启用，0禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
} 