package com.cg.controller.admin;

import com.cg.entity.User;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户控制器
 * 处理登录、注册等用户相关请求
 */
@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 显示登录页面
     * Spring Security 会处理 POST /user/login 的登录请求
     */
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }
    
    /**
     * 显示注册页面
     */
    @GetMapping("/register")
    public String register() {
        return "user/register";
    }
    
    /**
     * 处理用户注册
     */
    @PostMapping("/register")
    public String doRegister(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String email,
            Model model) {
        
        // 检查用户名是否已存在
        if (userService.getUserByUsername(username) != null) {
            model.addAttribute("error", "用户名已存在，请选择其他用户名");
            return "user/register";
        }
        
        // 创建用户对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        // 如果邮箱为空或空字符串，设置为null，避免唯一索引冲突
        user.setEmail(email != null && !email.trim().isEmpty() ? email : null);
        user.setNickname(username); // 默认昵称为用户名
        
        // 执行注册
        if (userService.register(user)) {
            model.addAttribute("success", "注册成功！请登录");
            return "user/login";
        } else {
            model.addAttribute("error", "注册失败，请重试");
            return "user/register";
        }
    }
    
    /**
     * 用户后台首页（重定向到文章列表）
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/user/article/list";
    }
}
