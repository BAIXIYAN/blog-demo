package com.cg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cg.entity.ArticleFavorite;

/**
 * 文章收藏服务接口
 */
public interface ArticleFavoriteService extends IService<ArticleFavorite> {

    /**
     * 收藏文章
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean favoriteArticle(Long articleId, Long userId);

    /**
     * 取消收藏
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean unfavoriteArticle(Long articleId, Long userId);

    /**
     * 检查用户是否已收藏
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否已收藏
     */
    boolean isFavorited(Long articleId, Long userId);

    /**
     * 获取文章收藏数
     * @param articleId 文章ID
     * @return 收藏数
     */
    long getFavoriteCount(Long articleId);
}

