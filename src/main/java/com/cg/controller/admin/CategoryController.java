package com.cg.controller.admin;

import com.cg.entity.Category;
import com.cg.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员文章类别管理控制器
 */
@Controller
@RequestMapping("/admin/category")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 分类列表页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<Category> categories = categoryService.listOrderBySort();
        model.addAttribute("categories", categories);
        return "admin/category-list";
    }
    
    /**
     * 添加分类页面
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-form";
    }
    
    /**
     * 编辑分类页面
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getById(id);
        if (category == null) {
            return "redirect:/admin/category/list";
        }
        model.addAttribute("category", category);
        return "admin/category-form";
    }
    
    /**
     * 保存分类（新增或更新）
     */
    @PostMapping("/save")
    public String save(Category category, RedirectAttributes redirectAttributes) {
        if (category.getId() == null) {
            // 新增：检查名称是否存在
            if (categoryService.existsByName(category.getName())) {
                redirectAttributes.addFlashAttribute("error", "分类名称已存在");
                return "redirect:/admin/category/add";
            }
        } else {
            // 编辑：如果名称改变，检查新名称是否存在
            Category existingCategory = categoryService.getById(category.getId());
            if (existingCategory != null 
                && !category.getName().equals(existingCategory.getName()) 
                && categoryService.existsByName(category.getName())) {
                redirectAttributes.addFlashAttribute("error", "分类名称已存在");
                return "redirect:/admin/category/edit/" + category.getId();
            }
        }
        
        // 设置默认值
        if (category.getSort() == null) {
            category.setSort(0);
        }
        if (category.getId() == null && category.getCreateTime() == null) {
            category.setCreateTime(LocalDateTime.now());
        }
        
        // 保存
        categoryService.saveOrUpdate(category);
        redirectAttributes.addFlashAttribute("message", category.getId() == null ? "分类添加成功" : "分类修改成功");
        return "redirect:/admin/category/list";
    }
    
    /**
     * 删除分类
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // 检查分类是否被文章使用
        // 这里简化处理，实际应该检查是否有文章使用此分类
        try {
            categoryService.removeById(id);
            redirectAttributes.addFlashAttribute("message", "分类删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败，该分类可能正在被使用");
        }
        return "redirect:/admin/category/list";
    }
}
