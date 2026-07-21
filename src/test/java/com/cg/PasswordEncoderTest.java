package com.cg;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码加密测试类
 * 用于生成BCrypt加密后的密码值，方便直接插入数据库
 */
@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 生成加密后的密码
     * 运行此测试方法，控制台会打印出加密后的密码值
     * 可以直接复制到SQL脚本中使用
     */
    @Test
    public void generateEncodedPassword() {
        // 要加密的原始密码
        String rawPassword = "admin123";
        
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

    /**
     * 生成多个常用密码的加密值
     */
    @Test
    public void generateMultiplePasswords() {
        String[] passwords = {"admin123", "123456", "password", "admin"};
        
        System.out.println("========================================");
        System.out.println("常用密码加密结果:");
        System.out.println("========================================");
        
        for (String password : passwords) {
            String encoded = passwordEncoder.encode(password);
            System.out.println("原始密码: " + password);
            System.out.println("加密后: " + encoded);
            System.out.println("---");
        }
        
        System.out.println("========================================");
    }
}

