package com.cg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cg.entity.ArticleFavorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章收藏Mapper接口
 */
@Mapper
public interface ArticleFavoriteMapper extends BaseMapper<ArticleFavorite> {
}

