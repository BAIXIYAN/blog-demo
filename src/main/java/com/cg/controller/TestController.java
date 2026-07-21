package com.cg.controller;

import com.cg.entity.User;
import com.cg.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试控制器
 * 用于验证数据库连接和实体类是否正确
 */
@RestController
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 测试数据库连接
     * 访问：http://localhost:8080/test/db
     */
    @GetMapping("/db")
    public String testDatabase() {
        try {
            List<User> users = userMapper.selectList(null);
            return "数据库连接成功！用户数量：" + users.size();
        } catch (Exception e) {
            return "数据库连接失败：" + e.getMessage();
        }
    }
    
    /**
     * 测试用户查询
     * 访问：http://localhost:8080/test/users
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        return userMapper.selectList(null);
    }
}