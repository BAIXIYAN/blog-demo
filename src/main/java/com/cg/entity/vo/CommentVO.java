package com.cg.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论视图对象
 */
@Data
public class CommentVO {
    
    private Long id;
    private String content;
    private Long articleId;
    private Long userId;
    private Long parentId;
    private LocalDateTime createTime;
    
    // 用户信息
    private String username;
    private String nickname;
    private String avatar;
    
    // 文章信息
    private String articleTitle;
    
    // 回复信息（如果是回复评论，显示被回复的用户信息）
    private Long replyToUserId;  // 被回复的用户ID
    private String replyToUsername;  // 被回复的用户名
    private String replyToNickname;  // 被回复的用户昵称
    
    // 子评论列表（嵌套回复）
    private List<CommentVO> children = new ArrayList<>();
    
    // 格式化时间
    public String getFormatCreateTime() {
        return createTime != null ? createTime.toString().replace("T", " ") : "";
    }
}