package com.cg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 个人博客系统启动类
 *
 * @author [你的名字]
 * @since 2025-12-25
 * @version 1.0
 */
@SpringBootApplication
@MapperScan("com.cg.mapper")  // 添加这行，方便后面自动扫描Mapper
public class BokeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BokeApplication.class, args);

        // 可选：添加启动成功提示
        System.out.println("✅ 个人博客系统启动成功！");
        System.out.println("🌐 访问地址: http://localhost:8080");
        System.out.println("🔑 管理员账号: admin / admin123");
    }
}