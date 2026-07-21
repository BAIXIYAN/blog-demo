package com.cg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cg.entity.Article;
import com.cg.mapper.ArticleMapper;
import com.cg.mapper.CommentMapper;
import com.cg.mapper.UserMapper;
import com.cg.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    
    @Autowired
    private ArticleMapper articleMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Override
    public long getTotalArticles() {
        return articleMapper.selectCount(null);
    }
    
    @Override
    public long getPublishedArticles() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1); // 已发布
        return articleMapper.selectCount(queryWrapper);
    }
    
    @Override
    public long getPendingArticles() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0); // 待审核
        return articleMapper.selectCount(queryWrapper);
    }
    
    @Override
    public long getTotalUsers() {
        return userMapper.selectCount(null);
    }
    
    @Override
    public long getTotalComments() {
        return commentMapper.selectCount(null);
    }
    
    @Override
    public long getTotalViews() {
        // 查询所有文章，累加浏览量
        return articleMapper.selectList(null)
                .stream()
                .mapToLong(article -> article.getViewCount() != null ? article.getViewCount() : 0L)
                .sum();
    }
}

