package com.cg.controller.admin;

import com.cg.entity.Article;
import com.cg.entity.Category;
import com.cg.entity.User;
import com.cg.entity.vo.ArticleVO;
import com.cg.entity.vo.CommentVO;
import com.cg.service.ArticleService;
import com.cg.service.CategoryService;
import com.cg.service.CommentService;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 用户后台文章管理控制器
 */
@Controller
@RequestMapping("/user/article")
public class ArticleAdminController {
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CommentService commentService;
    
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
     * 文章列表页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        
        List<ArticleVO> articles = articleService.getArticlesByUser(currentUser.getId());
        model.addAttribute("articles", articles);
        return "user/article-list";
    }
    
    /**
     * 发布文章页面
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        
        List<Category> categories = categoryService.list();
        model.addAttribute("categories", categories);
        model.addAttribute("article", new Article());
        return "user/article-form";
    }
    
    /**
     * 编辑文章页面
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        
        Article article = articleService.getById(id);
        if (article == null || !article.getUserId().equals(currentUser.getId())) {
            return "redirect:/user/article/list";
        }
        
        List<Category> categories = categoryService.list();
        model.addAttribute("categories", categories);
        model.addAttribute("article", article);
        return "user/article-form";
    }
    
    /**
     * 保存文章（发布或更新）
     */
    @PostMapping("/save")
    public String save(Article article, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        
        // 设置作者ID
        article.setUserId(currentUser.getId());
        
        if (article.getId() == null) {
            // 新增文章
            articleService.publishArticle(article);
            redirectAttributes.addFlashAttribute("message", "文章发布成功，等待审核");
        } else {
            // 更新文章
            Article existingArticle = articleService.getById(article.getId());
            if (existingArticle == null || !existingArticle.getUserId().equals(currentUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "无权修改此文章");
                return "redirect:/user/article/list";
            }
            
            // 更新文章信息
            existingArticle.setTitle(article.getTitle());
            existingArticle.setContent(article.getContent());
            existingArticle.setSummary(article.getSummary());
            existingArticle.setCategoryId(article.getCategoryId());
            existingArticle.setCoverImage(article.getCoverImage());
            // 如果文章已发布，修改后需要重新审核
            if (existingArticle.getStatus() == 1) {
                existingArticle.setStatus(0);
            }
            
            articleService.updateById(existingArticle);
            redirectAttributes.addFlashAttribute("message", "文章修改成功，等待重新审核");
        }
        
        return "redirect:/user/article/list";
    }
    
    /**
     * 删除文章
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        
        Article article = articleService.getById(id);
        if (article == null || !article.getUserId().equals(currentUser.getId())) {
            redirectAttributes.addFlashAttribute("error", "无权删除此文章");
            return "redirect:/user/article/list";
        }
        
        articleService.removeById(id);
        redirectAttributes.addFlashAttribute("message", "文章删除成功");
        return "redirect:/user/article/list";
    }
    
    /**
     * 查看文章详情（包括点击量和评论）
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        
        ArticleVO article = articleService.getArticleVOById(id);
        if (article == null || !article.getUserId().equals(currentUser.getId())) {
            return "redirect:/user/article/list";
        }
        
        // 获取评论列表
        List<CommentVO> comments = commentService.getCommentsByArticle(id);
        
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        return "user/article-detail";
    }
}
