package com.cg.controller.user;

import com.cg.entity.Notification;
import com.cg.entity.User;
import com.cg.service.NotificationService;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息通知控制器
 */
@Controller
@RequestMapping("/user/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        if (username == null || "anonymousUser".equals(username)) {
            return null;
        }
        return userService.getUserByUsername(username);
    }

    /**
     * 消息列表页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }

        List<Notification> notifications = notificationService.getNotificationsByUser(currentUser.getId());
        long unreadCount = notificationService.getUnreadCount(currentUser.getId());

        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);

        return "user/notification-list";
    }

    /**
     * 获取未读消息数量（AJAX接口）
     */
    @GetMapping("/unread-count")
    @ResponseBody
    public Map<String, Object> getUnreadCount() {
        Map<String, Object> result = new HashMap<>();
        
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            result.put("count", 0);
            return result;
        }

        long count = notificationService.getUnreadCount(currentUser.getId());
        result.put("count", count);
        
        return result;
    }

    /**
     * 标记通知为已读
     */
    @PostMapping("/read/{id}")
    @ResponseBody
    public Map<String, Object> markAsRead(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            result.put("success", false);
            result.put("message", "请先登录");
            return result;
        }

        boolean success = notificationService.markAsRead(id, currentUser.getId());
        result.put("success", success);
        result.put("message", success ? "标记成功" : "标记失败");
        
        return result;
    }

    /**
     * 标记所有通知为已读
     */
    @PostMapping("/read-all")
    @ResponseBody
    public Map<String, Object> markAllAsRead() {
        Map<String, Object> result = new HashMap<>();
        
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            result.put("success", false);
            result.put("message", "请先登录");
            return result;
        }

        boolean success = notificationService.markAllAsRead(currentUser.getId());
        result.put("success", success);
        result.put("message", success ? "全部标记成功" : "标记失败");
        
        return result;
    }
}

