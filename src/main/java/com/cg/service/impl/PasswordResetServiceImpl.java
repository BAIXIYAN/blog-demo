package com.cg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cg.entity.PasswordResetToken;
import com.cg.entity.User;
import com.cg.mapper.PasswordResetTokenMapper;
import com.cg.mapper.UserMapper;
import com.cg.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private PasswordResetTokenMapper tokenMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String createResetToken(String username) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username)
        );

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        tokenMapper.delete(
                new LambdaQueryWrapper<PasswordResetToken>().eq(PasswordResetToken::getUserId, user.getId())
        );

        String token = UUID.randomUUID().toString().replace("-", "");

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUserId(user.getId());
        resetToken.setToken(token);
        resetToken.setExpireTime(LocalDateTime.now().plusHours(1));

        tokenMapper.insert(resetToken);

        return token;
    }

    @Override
    public PasswordResetToken validateToken(String token) {
        PasswordResetToken resetToken = tokenMapper.selectOne(
                new LambdaQueryWrapper<PasswordResetToken>().eq(PasswordResetToken::getToken, token)
        );

        if (resetToken == null) {
            throw new RuntimeException("重置链接无效");
        }

        if (resetToken.isExpired()) {
            tokenMapper.deleteById(resetToken.getId());
            throw new RuntimeException("重置链接已过期");
        }

        return resetToken;
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = validateToken(token);

        User user = userMapper.selectById(resetToken.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);

        tokenMapper.deleteById(resetToken.getId());
    }
}
