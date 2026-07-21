package com.cg.service;

/**
 * 统计服务接口
 */
public interface StatisticsService {
    
    /**
     * 获取总文章数
     */
    long getTotalArticles();
    
    /**
     * 获取已发布文章数
     */
    long getPublishedArticles();
    
    /**
     * 获取待审核文章数
     */
    long getPendingArticles();
    
    /**
     * 获取总用户数
     */
    long getTotalUsers();
    
    /**
     * 获取总评论数
     */
    long getTotalComments();
    
    /**
     * 获取总浏览量
     */
    long getTotalViews();
}

