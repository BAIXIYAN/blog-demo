package com.cg.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章实体类
 * 对应数据库表：article
 *
 * @author [你的名字]
 * @since 2025-12-25
 */
@Data
@TableName("article")
public class Article {

    /**
     * 文章ID（主键，自增长）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容（HTML格式）
     */
    private String content;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 封面图片URL
     */
    private String coverImage;

    /**
     * 作者ID（关联sys_user.id）
     */
    private Long userId;

    /**
     * 分类ID（关联article_category.id）
     */
    private Long categoryId;

    /**
     * 文章状态：0-待审核, 1-已发布, 2-已拒绝
     */
    private Integer status;

    /**
     * 浏览量
     */
    private Integer viewCount;

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

    /**
     * 发布时间（审核通过时设置）
     */
    private LocalDateTime publishTime;
}