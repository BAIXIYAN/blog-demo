package com.cg.controller.user;

import com.cg.entity.User;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户个人资料管理控制器
 */
@Controller
@RequestMapping("/user/profile")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 获取当前登录用户
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (username == null || "anonymousUser".equals(username)) {
            return null;
        }
        return userService.getUserByUsername(username);
    }

    /**
     * 个人资料页面
     */
    @GetMapping
    public String profile(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }

        // 隐藏密码，不传递给前端
        currentUser.setPassword(null);
        model.addAttribute("user", currentUser);
        return "user/profile";
    }

    /**
     * 更新个人资料（昵称、邮箱、头像）
     */
    @PostMapping("/update")
    public String updateProfile(
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String avatar,
            @RequestParam(required = false) MultipartFile avatarFile,
            RedirectAttributes redirectAttributes) {

        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }

        // 重新获取最新用户信息
        User user = userService.getById(currentUser.getId());
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "用户不存在");
            return "redirect:/user/profile";
        }

        // 更新信息
        if (nickname != null && !nickname.trim().isEmpty()) {
            user.setNickname(nickname.trim());
        }
        
        if (email != null) {
            // 如果邮箱为空字符串，设置为null
            user.setEmail(email.trim().isEmpty() ? null : email.trim());
        }
        
        // 处理头像上传
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String avatarUrl = uploadAvatar(avatarFile, user.getId());
                if (avatarUrl != null) {
                    user.setAvatar(avatarUrl);
                } else {
                    redirectAttributes.addFlashAttribute("error", "头像上传失败，请重试");
                    return "redirect:/user/profile";
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "头像上传失败：" + e.getMessage());
                return "redirect:/user/profile";
            }
        } else if (avatar != null) {
            // 如果没有上传文件，使用URL方式
            user.setAvatar(avatar.trim().isEmpty() ? null : avatar.trim());
        }

        user.setUpdateTime(LocalDateTime.now());
        userService.updateById(user);

        redirectAttributes.addFlashAttribute("message", "个人资料更新成功");
        return "redirect:/user/profile";
    }
    
    /**
     * 上传头像文件
     */
    private String uploadAvatar(MultipartFile file, Long userId) throws IOException {
        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        
        String extension = "";
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = originalFilename.substring(lastDotIndex).toLowerCase();
        }
        
        // 只允许图片格式
        if (!extension.matches("\\.(jpg|jpeg|png|gif|webp)")) {
            throw new IllegalArgumentException("只支持 jpg、jpeg、png、gif、webp 格式的图片");
        }
        
        // 验证文件大小（5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("图片大小不能超过 5MB");
        }
        
        // 创建上传目录
        String projectPath = System.getProperty("user.dir");
        String uploadDir = projectPath + File.separator + "uploads" + File.separator + "avatars" + File.separator;
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 生成唯一文件名：用户ID_时间戳_UUID.扩展名
        String fileName = userId + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace("-", "") + extension;
        String filePath = uploadDir + fileName;
        
        // 保存文件
        File destFile = new File(filePath);
        file.transferTo(destFile);
        
        // 返回访问URL
        return "/uploads/avatars/" + fileName;
    }

    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }

        // 验证新密码和确认密码是否一致
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "新密码和确认密码不一致");
            return "redirect:/user/profile";
        }

        // 验证新密码长度
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "新密码长度不能少于6位");
            return "redirect:/user/profile";
        }

        // 重新获取最新用户信息
        User user = userService.getById(currentUser.getId());
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "用户不存在");
            return "redirect:/user/profile";
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "原密码错误");
            return "redirect:/user/profile";
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        userService.updateById(user);

        redirectAttributes.addFlashAttribute("message", "密码修改成功，请重新登录");
        return "redirect:/user/profile";
    }
}

