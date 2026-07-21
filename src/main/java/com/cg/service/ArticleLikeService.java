package com.cg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cg.entity.ArticleLike;

/**
 * 文章点赞服务接口
 */
public interface ArticleLikeService extends IService<ArticleLike> {

    /**
     * 点赞文章
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean likeArticle(Long articleId, Long userId);

    /**
     * 取消点赞
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean unlikeArticle(Long articleId, Long userId);

    /**
     * 检查用户是否已点赞
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否已点赞
     */
    boolean isLiked(Long articleId, Long userId);

    /**
     * 获取文章点赞数
     * @param articleId 文章ID
     * @return 点赞数
     */
    long getLikeCount(Long articleId);
}

