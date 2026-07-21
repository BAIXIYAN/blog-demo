package com.cg.controller.admin;

import com.cg.entity.User;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

/**
 * 管理员初始化控制器
 * 用于创建或重置管理员账号（仅开发环境使用）
 */
@Controller
@RequestMapping("/admin/init")
public class AdminInitController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * 显示管理员初始化页面
     */
    @GetMapping
    public String initPage(Model model) {
        User admin = userService.getUserByUsername("admin");
        model.addAttribute("adminExists", admin != null);
        if (admin != null) {
            model.addAttribute("adminStatus", admin.getStatus());
            model.addAttribute("adminRole", admin.getRole());
        }
        return "admin/init";
    }
    
    /**
     * 创建或重置管理员账号
     */
    @PostMapping("/create")
    public String createAdmin(
            @RequestParam(required = false, defaultValue = "admin") String username,
            @RequestParam String password,
            @RequestParam(required = false) String email,
            RedirectAttributes redirectAttributes) {
        
        try {
            User existingAdmin = userService.getUserByUsername(username);
            
            if (existingAdmin != null) {
                // 更新现有管理员
                existingAdmin.setPassword(passwordEncoder.encode(password));
                if (email != null && !email.trim().isEmpty()) {
                    existingAdmin.setEmail(email);
                }
                existingAdmin.setRole("admin");
                existingAdmin.setStatus(1);
                existingAdmin.setUpdateTime(LocalDateTime.now());
                userService.updateById(existingAdmin);
                redirectAttributes.addFlashAttribute("message", 
                    "管理员账号 '" + username + "' 已重置！新密码：" + password);
            } else {
                // 创建新管理员
                User newAdmin = new User();
                newAdmin.setUsername(username);
                newAdmin.setPassword(passwordEncoder.encode(password));
                newAdmin.setEmail(email != null && !email.trim().isEmpty() ? email : "admin@example.com");
                newAdmin.setNickname("系统管理员");
                newAdmin.setRole("admin");
                newAdmin.setStatus(1);
                newAdmin.setCreateTime(LocalDateTime.now());
                newAdmin.setUpdateTime(LocalDateTime.now());
                userService.save(newAdmin);
                redirectAttributes.addFlashAttribute("message", 
                    "管理员账号 '" + username + "' 创建成功！密码：" + password);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "操作失败：" + e.getMessage());
        }
        
        return "redirect:/admin/init";
    }
}



