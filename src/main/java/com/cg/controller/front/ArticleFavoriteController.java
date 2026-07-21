package com.cg.controller.front;

import com.cg.entity.User;
import com.cg.service.ArticleFavoriteService;
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
 * 文章收藏控制器
 */
@RestController
@RequestMapping("/api/favorites")
public class ArticleFavoriteController {
    
    @Autowired
    private ArticleFavoriteService articleFavoriteService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * 收藏/取消收藏文章
     */
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleFavorite(@RequestParam Long articleId) {
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
        
        // 检查是否已收藏
        boolean isFavorited = articleFavoriteService.isFavorited(articleId, user.getId());
        
        if (isFavorited) {
            // 取消收藏
            articleFavoriteService.unfavoriteArticle(articleId, user.getId());
            result.put("success", true);
            result.put("favorited", false);
            result.put("message", "已取消收藏");
        } else {
            // 收藏
            articleFavoriteService.favoriteArticle(articleId, user.getId());
            // 创建收藏通知
            notificationService.createFavoriteNotification(articleId, user.getId());
            result.put("success", true);
            result.put("favorited", true);
            result.put("message", "收藏成功");
        }
        
        // 获取更新后的收藏数
        long favoriteCount = articleFavoriteService.getFavoriteCount(articleId);
        result.put("favoriteCount", favoriteCount);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取文章收藏状态
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getFavoriteStatus(@RequestParam Long articleId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // 获取收藏数
        long favoriteCount = articleFavoriteService.getFavoriteCount(articleId);
        result.put("favoriteCount", favoriteCount);
        
        // 检查是否已登录
        if (username == null || "anonymousUser".equals(username)) {
            result.put("isFavorited", false);
            return ResponseEntity.ok(result);
        }
        
        // 根据用户名获取用户信息
        User user = userService.getUserByUsername(username);
        if (user != null) {
            boolean isFavorited = articleFavoriteService.isFavorited(articleId, user.getId());
            result.put("isFavorited", isFavorited);
        } else {
            result.put("isFavorited", false);
        }
        
        return ResponseEntity.ok(result);
    }
}

