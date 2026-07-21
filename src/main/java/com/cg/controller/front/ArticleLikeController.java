package com.cg.controller.front;

import com.cg.entity.User;
import com.cg.service.ArticleLikeService;
import com.cg.service.NotificationService;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 文章点赞控制器
 */
@RestController
@RequestMapping("/api/likes")
public class ArticleLikeController {
    
    @Autowired
    private ArticleLikeService articleLikeService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * 点赞/取消点赞文章
     */
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleLike(@RequestParam Long articleId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // 检查是否已登录
        if (username == null || "anonymousUser".equals(username)) {
            result.put("success", false);
            result.put("message", "请先登录");
            return ResponseEntity.ok(result);
        }
        
        // 根据用户名获取用户信息
        User user = userService.getUserByUsername(username);
        if (user == null) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        
        // 检查是否已点赞
        boolean isLiked = articleLikeService.isLiked(articleId, user.getId());
        
        if (isLiked) {
            // 取消点赞
            articleLikeService.unlikeArticle(articleId, user.getId());
            result.put("success", true);
            result.put("liked", false);
            result.put("message", "已取消点赞");
        } else {
            // 点赞
            articleLikeService.likeArticle(articleId, user.getId());
            // 创建点赞通知
            notificationService.createLikeNotification(articleId, user.getId());
            result.put("success", true);
            result.put("liked", true);
            result.put("message", "点赞成功");
        }
        
        // 获取更新后的点赞数
        long likeCount = articleLikeService.getLikeCount(articleId);
        result.put("likeCount", likeCount);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取文章点赞状态
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getLikeStatus(@RequestParam Long articleId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // 获取点赞数
        long likeCount = articleLikeService.getLikeCount(articleId);
        result.put("likeCount", likeCount);
        
        // 检查是否已登录
        if (username == null || "anonymousUser".equals(username)) {
            result.put("isLiked", false);
            return ResponseEntity.ok(result);
        }
        
        // 根据用户名获取用户信息
        User user = userService.getUserByUsername(username);
        if (user != null) {
            boolean isLiked = articleLikeService.isLiked(articleId, user.getId());
            result.put("isLiked", isLiked);
        } else {
            result.put("isLiked", false);
        }
        
        return ResponseEntity.ok(result);
    }
}

