package com.cg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cg.entity.ArticleLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章点赞Mapper接口
 */
@Mapper
public interface ArticleLikeMapper extends BaseMapper<ArticleLike> {
}

