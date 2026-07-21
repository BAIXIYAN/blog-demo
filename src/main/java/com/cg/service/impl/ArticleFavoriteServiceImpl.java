package com.cg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cg.entity.ArticleFavorite;
import com.cg.mapper.ArticleFavoriteMapper;
import com.cg.service.ArticleFavoriteService;
import org.springframework.stereotype.Service;

/**
 * 文章收藏服务实现类
 */
@Service
public class ArticleFavoriteServiceImpl extends ServiceImpl<ArticleFavoriteMapper, ArticleFavorite> implements ArticleFavoriteService {

    @Override
    public boolean favoriteArticle(Long articleId, Long userId) {
        // 检查是否已收藏
        if (isFavorited(articleId, userId)) {
            return false; // 已收藏，不能重复收藏
        }
        
        // 创建收藏记录
        ArticleFavorite favorite = new ArticleFavorite();
        favorite.setArticleId(articleId);
        favorite.setUserId(userId);
        
        return save(favorite);
    }

    @Override
    public boolean unfavoriteArticle(Long articleId, Long userId) {
        // 查找收藏记录
        QueryWrapper<ArticleFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId)
                   .eq("user_id", userId);
        
        return remove(queryWrapper);
    }

    @Override
    public boolean isFavorited(Long articleId, Long userId) {
        if (articleId == null || userId == null) {
            return false;
        }
        
        QueryWrapper<ArticleFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId)
                   .eq("user_id", userId);
        
        return count(queryWrapper) > 0;
    }

    @Override
    public long getFavoriteCount(Long articleId) {
        if (articleId == null) {
            return 0;
        }
        
        QueryWrapper<ArticleFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId);
        
        return count(queryWrapper);
    }
}

