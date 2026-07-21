package com.cg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cg.entity.User;

/**
 * 用户服务接口
 *
 * @author [你的名字]
 * @since 2025-12-25
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    boolean register(User user);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    boolean login(String username, String password);
}