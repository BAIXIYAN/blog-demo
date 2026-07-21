package com.cg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cg.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 *
 * @姚美妍
 * @since 2025-12-25
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承BaseMapper，已包含基本的CRUD方法
    // 可以在这里添加自定义的查询方法
}