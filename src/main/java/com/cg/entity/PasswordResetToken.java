package com.cg.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("password_reset_token")
public class PasswordResetToken {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String token;

    private LocalDateTime expireTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expireTime);
    }
}
