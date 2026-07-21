package com.cg.controller.admin;

import com.cg.entity.vo.CommentVO;
import com.cg.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 管理员评论管理控制器
 */
@Controller
@RequestMapping("/admin/comment")
public class CommentAdminController {
    
    @Autowired
    private CommentService commentService;
    
    /**
     * 评论管理列表页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<CommentVO> comments = commentService.getAllComments();
        model.addAttribute("comments", comments);
        return "admin/comment-list";
    }
    
    /**
     * 删除评论
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (commentService.removeById(id)) {
            redirectAttributes.addFlashAttribute("message", "评论已删除");
        } else {
            redirectAttributes.addFlashAttribute("error", "删除失败");
        }
        return "redirect:/admin/comment/list";
    }
}

