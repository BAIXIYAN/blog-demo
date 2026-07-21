package com.cg.controller.admin;

import com.cg.entity.User;
import com.cg.entity.vo.ArticleVO;
import com.cg.service.ArticleService;
import com.cg.service.StatisticsService;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员后台控制器
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private StatisticsService statisticsService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 管理员后台首页
     */
    @GetMapping("/index")
    public String index(Model model) {
        // 获取统计数据
        model.addAttribute("totalArticles", statisticsService.getTotalArticles());
        model.addAttribute("publishedArticles", statisticsService.getPublishedArticles());
        model.addAttribute("pendingArticles", statisticsService.getPendingArticles());
        model.addAttribute("totalUsers", statisticsService.getTotalUsers());
        model.addAttribute("totalComments", statisticsService.getTotalComments());
        model.addAttribute("totalViews", statisticsService.getTotalViews());
        return "admin/index";
    }
    
    /**
     * 文章审核列表页面
     */
    @GetMapping("/article/review")
    public String reviewList(@RequestParam(required = false, defaultValue = "0") Integer status, Model model) {
        List<ArticleVO> articles;
        if (status == 0) {
            // 待审核文章
            articles = articleService.getPendingArticles();
            model.addAttribute("status", 0);
            model.addAttribute("statusName", "待审核");
        } else {
            // 所有文章
            articles = articleService.getAllArticles();
            model.addAttribute("status", -1);
            model.addAttribute("statusName", "全部");
        }
        
        model.addAttribute("articles", articles);
        return "admin/article-review";
    }
    
    /**
     * 文章审核详情页面
     */
    @GetMapping("/article/detail/{id}")
    public String reviewDetail(@PathVariable Long id, Model model) {
        ArticleVO article = articleService.getArticleVOById(id);
        if (article == null) {
            return "redirect:/admin/article/review";
        }
        
        model.addAttribute("article", article);
        return "admin/article-review-detail";
    }
    
    /**
     * 审核通过
     */
    @PostMapping("/article/approve/{id}")
    public String approve(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (articleService.reviewArticle(id, 1)) {
            redirectAttributes.addFlashAttribute("message", "文章审核通过");
        } else {
            redirectAttributes.addFlashAttribute("error", "审核失败");
        }
        return "redirect:/admin/article/review";
    }
    
    /**
     * 审核拒绝
     */
    @PostMapping("/article/reject/{id}")
    public String reject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (articleService.reviewArticle(id, 2)) {
            redirectAttributes.addFlashAttribute("message", "文章已拒绝");
        } else {
            redirectAttributes.addFlashAttribute("error", "操作失败");
        }
        return "redirect:/admin/article/review";
    }
    
    /**
     * 用户管理列表页面
     */
    @GetMapping("/user/list")
    public String userList(Model model) {
        List<User> users = userService.list();
        model.addAttribute("users", users);
        return "admin/user-list";
    }
    
    /**
     * 启用用户
     */
    @PostMapping("/user/enable/{id}")
    public String enableUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.getById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "用户不存在");
            return "redirect:/admin/user/list";
        }
        
        // 不能禁用自己
        if ("admin".equals(user.getRole())) {
            redirectAttributes.addFlashAttribute("error", "不能操作管理员账号");
            return "redirect:/admin/user/list";
        }
        
        user.setStatus(1);
        user.setUpdateTime(LocalDateTime.now());
        if (userService.updateById(user)) {
            redirectAttributes.addFlashAttribute("message", "用户已启用");
        } else {
            redirectAttributes.addFlashAttribute("error", "操作失败");
        }
        return "redirect:/admin/user/list";
    }
    
    /**
     * 禁用用户
     */
    @PostMapping("/user/disable/{id}")
    public String disableUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.getById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "用户不存在");
            return "redirect:/admin/user/list";
        }
        
        // 不能禁用自己
        if ("admin".equals(user.getRole())) {
            redirectAttributes.addFlashAttribute("error", "不能操作管理员账号");
            return "redirect:/admin/user/list";
        }
        
        user.setStatus(0);
        user.setUpdateTime(LocalDateTime.now());
        if (userService.updateById(user)) {
            redirectAttributes.addFlashAttribute("message", "用户已禁用");
        } else {
            redirectAttributes.addFlashAttribute("error", "操作失败");
        }
        return "redirect:/admin/user/list";
    }
    
    /**
     * 删除用户
     */
    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.getById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "用户不存在");
            return "redirect:/admin/user/list";
        }
        
        // 不能删除管理员账号
        if ("admin".equals(user.getRole())) {
            redirectAttributes.addFlashAttribute("error", "不能删除管理员账号");
            return "redirect:/admin/user/list";
        }
        
        if (userService.removeById(id)) {
            redirectAttributes.addFlashAttribute("message", "用户已删除");
        } else {
            redirectAttributes.addFlashAttribute("error", "删除失败");
        }
        return "redirect:/admin/user/list";
    }
}
