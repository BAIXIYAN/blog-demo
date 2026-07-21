package com.cg.controller.front;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cg.entity.Category;
import com.cg.entity.vo.ArticleVO;
import com.cg.service.ArticleService;
import com.cg.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 首页控制器
 */
@Controller
public class HomeController {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ArticleService articleService;
    
    /**
     * 首页 - 按分类展示已审核的文章（支持分页和排序）
     */
    @GetMapping("/")
    public String index(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "publishTime") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            Model model) {
        // 获取所有分类（按排序字段排序）
        List<Category> categories = categoryService.listOrderBySort();
        model.addAttribute("categories", categories);
        
        // 获取文章列表（分页）
        IPage<ArticleVO> articlePage;
        if (categoryId != null && categoryId > 0) {
            // 按分类查询
            articlePage = articleService.getArticlesByCategoryPage(categoryId, page, size, sortField, sortOrder);
            model.addAttribute("selectedCategoryId", categoryId);
        } else {
            // 查询所有已审核文章
            articlePage = articleService.getArticleListPage(page, size, sortField, sortOrder);
            model.addAttribute("selectedCategoryId", 0);
        }
        
        model.addAttribute("articles", articlePage.getRecords());
        model.addAttribute("articlePage", articlePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        return "front/index";
    }

    /**
     * 首页（备用路径）
     */
    @GetMapping("/index")
    public String indexPage(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "publishTime") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            Model model) {
        return index(categoryId, page, size, sortField, sortOrder, model);
    }

    /**
     * 搜索文章（支持分页和排序）
     */
    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "publishTime") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            Model model) {
        // 获取所有分类（按排序字段排序）
        List<Category> categories = categoryService.listOrderBySort();
        model.addAttribute("categories", categories);
        
        // 获取搜索结果（分页）
        IPage<ArticleVO> articlePage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            articlePage = articleService.searchArticlesPage(keyword.trim(), page, size, sortField, sortOrder);
            model.addAttribute("keyword", keyword.trim());
            model.addAttribute("searchMode", true);
        } else {
            // 如果没有关键词，返回所有已发布文章
            articlePage = articleService.getArticleListPage(page, size, sortField, sortOrder);
            model.addAttribute("searchMode", false);
        }
        
        model.addAttribute("articles", articlePage.getRecords());
        model.addAttribute("articlePage", articlePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("selectedCategoryId", 0);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        return "front/index";
    }
}
