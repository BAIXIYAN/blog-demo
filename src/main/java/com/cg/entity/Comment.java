package com.cg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章评论实体类
 * 对应数据库表：article_comment
 *
 * @author [你的名字]
 * @since 2025-12-25
 */
@Data
@TableName("article_comment")
public class Comment {

    /**
     * 评论ID（主键，自增长）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 文章ID（关联article.id）
     */
    private Long articleId;

    /**
     * 用户ID（关联sys_user.id）
     */
    private Long userId;

    /**
     * 父评论ID（0表示顶级评论）
     */
    private Long parentId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}