package com.cg.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章视图对象（用于前端展示）
 * 
 * @author [你的名字]
 * @since 2025-12-25
 */
@Data
public class ArticleVO {
    
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String coverImage;
    private Long userId;
    private Long categoryId;
    private Integer status;
    private Integer viewCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime publishTime;
    
    // 额外信息
    private String categoryName;  // 分类名称
    private String authorName;    // 作者昵称
    private String authorAvatar;  // 作者头像
    private Long likeCount;       // 点赞数
    private Boolean isLiked;      // 当前用户是否已点赞
    private Long favoriteCount;   // 收藏数
    private Boolean isFavorited;  // 当前用户是否已收藏
    
    // 格式化时间
    public String getFormatCreateTime() {
        return createTime.toString().replace("T", " ");
    }
    
    public String getFormatPublishTime() {
        return publishTime != null ? publishTime.toString().replace("T", " ") : "";
    }
}