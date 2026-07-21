package com.cg.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码生成工具类
 * 用于生成BCrypt加密后的密码值
 * 
 * 使用方法：
 * 1. 直接运行main方法
 * 2. 修改main方法中的密码
 * 3. 查看控制台输出的加密值
 */
public class PasswordGenerator {
    
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // 要加密的原始密码（修改这里）
        String rawPassword = "123456";
        
        // 加密密码
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        // 打印结果
        System.out.println("========================================");
        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密后的密码: " + encodedPassword);
        System.out.println("========================================");
        System.out.println();
        System.out.println("SQL更新语句（复制使用）:");
        System.out.println("UPDATE `sys_user` SET `password` = '" + encodedPassword + "' WHERE `username` = 'admin';");
        System.out.println();
        System.out.println("或者完整更新语句（包含角色和状态）:");
        System.out.println("UPDATE `sys_user` SET ");
        System.out.println("    `password` = '" + encodedPassword + "',");
        System.out.println("    `role` = 'admin',");
        System.out.println("    `status` = 1,");
        System.out.println("    `update_time` = NOW()");
        System.out.println("WHERE `username` = 'admin';");
        System.out.println("========================================");
        
        // 验证密码是否正确
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("密码验证结果: " + (matches ? "✓ 正确" : "✗ 错误"));
    }
}



