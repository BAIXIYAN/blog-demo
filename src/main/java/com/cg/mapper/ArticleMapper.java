package com.cg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cg.entity.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章数据访问层
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}