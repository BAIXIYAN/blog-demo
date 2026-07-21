package com.cg.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息通知实体类
 * 对应数据库表：notification
 *
 * @author [你的名字]
 * @since 2025-12-28
 */
@Data
@TableName("notification")
public class Notification {

    /**
     * 通知ID（主键，自增长）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接收用户ID（文章作者）
     */
    private Long userId;

    /**
     * 触发用户ID（点赞/收藏的用户）
     */
    private Long triggerUserId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 通知类型：like-点赞, favorite-收藏
     */
    private String type;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 是否已读：0-未读, 1-已读
     */
    private Integer isRead;

    /**
     * 创建时间（插入时自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 读取时间
     */
    private LocalDateTime readTime;
}

