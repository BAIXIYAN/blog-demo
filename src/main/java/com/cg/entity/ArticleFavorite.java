package com.cg.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章收藏实体类
 * 对应数据库表：article_favorite
 *
 * @author [你的名字]
 * @since 2025-12-25
 */
@Data
@TableName("article_favorite")
public class ArticleFavorite {

    /**
     * 收藏ID（主键，自增长）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章ID（关联article.id）
     */
    private Long articleId;

    /**
     * 用户ID（关联sys_user.id）
     */
    private Long userId;

    /**
     * 创建时间（插入时自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

