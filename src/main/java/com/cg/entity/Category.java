package com.cg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章分类实体类
 * 对应数据库表：article_category
 *
 * @author [你的名字]
 * @since 2025-12-25
 */
@Data
@TableName("article_category")
public class Category {

    /**
     * 分类ID（主键，自增长）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称（唯一）
     */
    private String name;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 排序（数字越小越靠前）
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}