package com.cg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cg.entity.Notification;

import java.util.List;

/**
 * 消息通知服务接口
 */
public interface NotificationService extends IService<Notification> {

    /**
     * 创建点赞通知
     * @param articleId 文章ID
     * @param triggerUserId 触发用户ID（点赞的用户）
     * @return 是否成功
     */
    boolean createLikeNotification(Long articleId, Long triggerUserId);

    /**
     * 创建收藏通知
     * @param articleId 文章ID
     * @param triggerUserId 触发用户ID（收藏的用户）
     * @return 是否成功
     */
    boolean createFavoriteNotification(Long articleId, Long triggerUserId);

    /**
     * 获取用户的所有通知
     * @param userId 用户ID
     * @return 通知列表
     */
    List<Notification> getNotificationsByUser(Long userId);

    /**
     * 获取用户的未读通知数量
     * @param userId 用户ID
     * @return 未读数量
     */
    long getUnreadCount(Long userId);

    /**
     * 标记通知为已读
     * @param notificationId 通知ID
     * @param userId 用户ID（验证权限）
     * @return 是否成功
     */
    boolean markAsRead(Long notificationId, Long userId);

    /**
     * 标记所有通知为已读
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAllAsRead(Long userId);
}

