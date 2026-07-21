package com.cg.service;

import com.cg.entity.PasswordResetToken;

public interface PasswordResetService {

    String createResetToken(String username);

    PasswordResetToken validateToken(String token);

    void resetPassword(String token, String newPassword);
}
