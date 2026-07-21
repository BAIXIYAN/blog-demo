package com.cg.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cg.entity.Article;
import com.cg.entity.ArticleFavorite;
import com.cg.entity.ArticleLike;
import com.cg.entity.User;
import com.cg.entity.vo.ArticleVO;
import com.cg.service.ArticleFavoriteService;
import com.cg.service.ArticleLikeService;
import com.cg.service.ArticleService;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户收藏和点赞管理控制器
 */
@Controller
@RequestMapping("/user/favorite")
public class UserFavoriteController {
    
    @Autowired
    private ArticleFavoriteService articleFavoriteService;
    
    @Autowired
    private ArticleLikeService articleLikeService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 获取当前登录用户
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (username == null || "anonymousUser".equals(username)) {
            return null;
        }
        return userService.getUserByUsername(username);
    }
    
    /**
     * 我的收藏页面
     */
    @GetMapping("/list")
    public String favoriteList(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        
        // 获取用户收藏的文章ID列表
        QueryWrapper<ArticleFavorite> favoriteWrapper = new QueryWrapper<>();
        favoriteWrapper.eq("user_id", currentUser.getId())
                      .orderByDesc("create_time");
        List<ArticleFavorite> favorites = articleFavoriteService.list(favoriteWrapper);
        
        // 转换为文章列表
        List<ArticleVO> favoriteArticles = new ArrayList<>();
        for (ArticleFavorite favorite : favorites) {
            ArticleVO article = articleService.getArticleVOById(favorite.getArticleId(), currentUser.getId());
            if (article != null && article.getStatus() != null && article.getStatus() == 1) {
                favoriteArticles.add(article);
            }
        }
        
        model.addAttribute("articles", favoriteArticles);
        model.addAttribute("type", "favorite");
        model.addAttribute("title", "我的收藏");
        return "user/favorite-list";
    }
    
    /**
     * 我的点赞页面
     */
    @GetMapping("/like")
    public String likeList(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        
        // 获取用户点赞的文章ID列表
        QueryWrapper<ArticleLike> likeWrapper = new QueryWrapper<>();
        likeWrapper.eq("user_id", currentUser.getId())
                  .orderByDesc("create_time");
        List<ArticleLike> likes = articleLikeService.list(likeWrapper);
        
        // 转换为文章列表
        List<ArticleVO> likeArticles = new ArrayList<>();
        for (ArticleLike like : likes) {
            ArticleVO article = articleService.getArticleVOById(like.getArticleId(), currentUser.getId());
            if (article != null && article.getStatus() != null && article.getStatus() == 1) {
                likeArticles.add(article);
            }
        }
        
        model.addAttribute("articles", likeArticles);
        model.addAttribute("type", "like");
        model.addAttribute("title", "我的点赞");
        return "user/favorite-list";
    }
}

