package com.cg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cg.entity.Article;
import com.cg.entity.Notification;
import com.cg.entity.User;
import com.cg.mapper.NotificationMapper;
import com.cg.service.ArticleService;
import com.cg.service.NotificationService;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息通知服务实现类
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Override
    public boolean createLikeNotification(Long articleId, Long triggerUserId) {
        // 获取文章信息
        Article article = articleService.getById(articleId);
        if (article == null) {
            return false;
        }

        // 获取文章作者ID
        Long authorId = article.getUserId();
        
        // 如果点赞的是自己的文章，不发送通知
        if (authorId.equals(triggerUserId)) {
            return true;
        }

        // 获取触发用户信息
        User triggerUser = userService.getById(triggerUserId);
        if (triggerUser == null) {
            return false;
        }

        // 创建通知
        Notification notification = new Notification();
        notification.setUserId(authorId);
        notification.setTriggerUserId(triggerUserId);
        notification.setArticleId(articleId);
        notification.setType("like");
        String triggerUserName = triggerUser.getNickname() != null && !triggerUser.getNickname().isEmpty() 
            ? triggerUser.getNickname() : triggerUser.getUsername();
        notification.setContent(triggerUserName + " 点赞了您的文章《" + article.getTitle() + "》");
        notification.setIsRead(0);

        return save(notification);
    }

    @Override
    public boolean createFavoriteNotification(Long articleId, Long triggerUserId) {
        // 获取文章信息
        Article article = articleService.getById(articleId);
        if (article == null) {
            return false;
        }

        // 获取文章作者ID
        Long authorId = article.getUserId();
        
        // 如果收藏的是自己的文章，不发送通知
        if (authorId.equals(triggerUserId)) {
            return true;
        }

        // 获取触发用户信息
        User triggerUser = userService.getById(triggerUserId);
        if (triggerUser == null) {
            return false;
        }

        // 创建通知
        Notification notification = new Notification();
        notification.setUserId(authorId);
        notification.setTriggerUserId(triggerUserId);
        notification.setArticleId(articleId);
        notification.setType("favorite");
        String triggerUserName = triggerUser.getNickname() != null && !triggerUser.getNickname().isEmpty() 
            ? triggerUser.getNickname() : triggerUser.getUsername();
        notification.setContent(triggerUserName + " 收藏了您的文章《" + article.getTitle() + "》");
        notification.setIsRead(0);

        return save(notification);
    }

    @Override
    public List<Notification> getNotificationsByUser(Long userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
        
        return list(queryWrapper);
    }

    @Override
    public long getUnreadCount(Long userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("is_read", 0);
        
        return count(queryWrapper);
    }

    @Override
    public boolean markAsRead(Long notificationId, Long userId) {
        Notification notification = getById(notificationId);
        if (notification == null || !notification.getUserId().equals(userId)) {
            return false;
        }

        UpdateWrapper<Notification> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", notificationId)
                    .set("is_read", 1)
                    .set("read_time", LocalDateTime.now());

        return update(updateWrapper);
    }

    @Override
    public boolean markAllAsRead(Long userId) {
        UpdateWrapper<Notification> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId)
                    .eq("is_read", 0)
                    .set("is_read", 1)
                    .set("read_time", LocalDateTime.now());

        return update(updateWrapper);
    }
}

