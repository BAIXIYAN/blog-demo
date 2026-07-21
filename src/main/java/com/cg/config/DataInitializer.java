package com.cg.config;

import com.cg.entity.User;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 数据初始化类
 * 应用启动时自动创建默认管理员账号（如果不存在）
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // 检查admin用户是否存在
        User admin = userService.getUserByUsername("admin");
        if (admin == null) {
            // 创建默认管理员账号
            User defaultAdmin = new User();
            defaultAdmin.setUsername("admin");
            defaultAdmin.setPassword(passwordEncoder.encode("admin123")); // 默认密码：admin123
            defaultAdmin.setEmail("admin@example.com");
            defaultAdmin.setNickname("系统管理员");
            defaultAdmin.setRole("admin");
            defaultAdmin.setStatus(1);
            defaultAdmin.setCreateTime(LocalDateTime.now());
            defaultAdmin.setUpdateTime(LocalDateTime.now());
            
            userService.save(defaultAdmin);
            System.out.println("✅ 默认管理员账号已创建：admin / admin123");
        } else {
            System.out.println("ℹ️  管理员账号已存在：admin");
        }
    }
}

