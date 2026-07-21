package com.cg.controller.front;

import com.cg.entity.User;
import com.cg.entity.vo.ArticleVO;
import com.cg.entity.vo.CommentVO;
import com.cg.service.ArticleService;
import com.cg.service.CommentService;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 前台文章控制器
 */
@Controller
public class ArticleController {
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 文章详情页
     */
    @GetMapping("/articles/{id}")
    public String detail(@PathVariable Long id, 
                        @org.springframework.web.bind.annotation.RequestParam(required = false) String from,
                        Model model) {
        // 获取当前登录用户ID
        Long userId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            if (username != null && !"anonymousUser".equals(username)) {
                User user = userService.getUserByUsername(username);
                if (user != null) {
                    userId = user.getId();
                }
            }
        }
        
        // 获取文章详情（带用户ID，用于判断是否已点赞）
        ArticleVO article = articleService.getArticleVOById(id, userId);
        if (article == null || article.getStatus() == null || article.getStatus() != 1) {
            // 文章不存在或未审核通过
            return "redirect:/";
        }
        
        // 增加浏览量
        articleService.increaseViewCount(id);
        // 重新获取以更新浏览量
        article = articleService.getArticleVOById(id, userId);
        
        // 获取评论列表
        List<CommentVO> comments = commentService.getCommentsByArticle(id);
        
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        model.addAttribute("from", from); // 记录来源页面
        
        return "front/article-detail";
    }
}
