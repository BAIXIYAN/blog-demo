package com.cg.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库表：sys_user
 *
 * @author [你的名字]
 * @since 2025-12-25
 */
@Data
@TableName("sys_user")
public class User {

    /**
     * 用户ID（主键，自增长）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名（唯一，用于登录）
     */
    private String username;

    /**
     * 密码（加密存储）
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 用户角色：admin-管理员, user-普通用户
     */
    private String role;

    /**
     * 用户状态：0-禁用, 1-正常
     */
    private Integer status;

    /**
     * 创建时间（插入时自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间（插入和更新时自动填充）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}