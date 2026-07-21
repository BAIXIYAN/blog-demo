package com.cg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cg.entity.ArticleLike;
import com.cg.mapper.ArticleLikeMapper;
import com.cg.service.ArticleLikeService;
import org.springframework.stereotype.Service;

/**
 * 文章点赞服务实现类
 */
@Service
public class ArticleLikeServiceImpl extends ServiceImpl<ArticleLikeMapper, ArticleLike> implements ArticleLikeService {

    @Override
    public boolean likeArticle(Long articleId, Long userId) {
        // 检查是否已点赞
        if (isLiked(articleId, userId)) {
            return false; // 已点赞，不能重复点赞
        }
        
        // 创建点赞记录
        ArticleLike like = new ArticleLike();
        like.setArticleId(articleId);
        like.setUserId(userId);
        
        return save(like);
    }

    @Override
    public boolean unlikeArticle(Long articleId, Long userId) {
        // 查找点赞记录
        QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId)
                   .eq("user_id", userId);
        
        return remove(queryWrapper);
    }

    @Override
    public boolean isLiked(Long articleId, Long userId) {
        if (articleId == null || userId == null) {
            return false;
        }
        
        QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId)
                   .eq("user_id", userId);
        
        return count(queryWrapper) > 0;
    }

    @Override
    public long getLikeCount(Long articleId) {
        if (articleId == null) {
            return 0;
        }
        
        QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId);
        
        return count(queryWrapper);
    }
}

