package com.cg.controller.front;

import com.cg.entity.Comment;
import com.cg.entity.User;
import com.cg.service.CommentService;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 前台评论控制器
 */
@Controller
@RequestMapping("/comments")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 发表评论
     */
    @PostMapping("/add")
    public String addComment(
            @RequestParam Long articleId,
            @RequestParam String content,
            @RequestParam(required = false, defaultValue = "0") Long parentId) {
        
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // 检查是否已登录
        if (username == null || "anonymousUser".equals(username)) {
            return "redirect:/user/login";
        }
        
        // 根据用户名获取用户信息
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return "redirect:/user/login";
        }
        
        // 创建评论对象
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setContent(content);
        comment.setParentId(parentId != null ? parentId : 0L);
        comment.setUserId(user.getId());
        
        // 保存评论
        commentService.addComment(comment);
        
        // 重定向到文章详情页
        return "redirect:/articles/" + articleId;
    }
}
