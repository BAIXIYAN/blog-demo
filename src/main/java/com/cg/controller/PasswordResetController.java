package com.cg.controller;

import com.cg.entity.PasswordResetToken;
import com.cg.service.PasswordResetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/password")
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/forgot")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot")
    @ResponseBody
    public Map<String, Object> processForgotPassword(@RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        try {
            String token = passwordResetService.createResetToken(username);
            String resetUrl = "http://localhost:8080/password/reset?token=" + token;
            
            logger.info("=== Password Reset Link ===");
            logger.info("Username: {}", username);
            logger.info("Reset URL: {}", resetUrl);
            logger.info("==========================");
            
            result.put("success", true);
            result.put("message", "重置链接已生成（请查看控制台日志）");
            result.put("resetUrl", resetUrl);
            result.put("token", token);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/reset")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        try {
            PasswordResetToken resetToken = passwordResetService.validateToken(token);
            model.addAttribute("token", token);
            return "reset-password";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/password/forgot?error=" + e.getMessage();
        }
    }

    @PostMapping("/reset")
    @ResponseBody
    public Map<String, Object> processResetPassword(
            @RequestParam String token,
            @RequestParam String password,
            @RequestParam String confirmPassword) {
        Map<String, Object> result = new HashMap<>();
        
        if (!password.equals(confirmPassword)) {
            result.put("success", false);
            result.put("message", "两次输入的密码不一致");
            return result;
        }
        
        if (password.length() < 6) {
            result.put("success", false);
            result.put("message", "密码长度至少6位");
            return result;
        }
        
        try {
            passwordResetService.resetPassword(token, password);
            result.put("success", true);
            result.put("message", "密码重置成功，请登录");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}
