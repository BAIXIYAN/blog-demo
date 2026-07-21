package com.cg.controller.user;

import com.cg.entity.User;
import com.cg.entity.vo.CommentVO;
import com.cg.service.CommentService;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 用户评论管理控制器
 */
@Controller
@RequestMapping("/user/comment")
public class CommentUserController {
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户评论列表页面
     */
    @GetMapping("/list")
    public String list(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/user/login";
        }
        
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        
        List<CommentVO> comments = commentService.getCommentsByUser(currentUser.getId());
        model.addAttribute("comments", comments);
        return "user/comment-list";
    }
    
    /**
     * 删除评论
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, 
                        @AuthenticationPrincipal UserDetails userDetails,
                        RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/user/login";
        }
        
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        
        // 检查评论是否属于当前用户
        com.cg.entity.Comment comment = commentService.getById(id);
        if (comment == null) {
            redirectAttributes.addFlashAttribute("error", "评论不存在");
            return "redirect:/user/comment/list";
        }
        
        if (!comment.getUserId().equals(currentUser.getId())) {
            redirectAttributes.addFlashAttribute("error", "无权删除此评论");
            return "redirect:/user/comment/list";
        }
        
        if (commentService.removeById(id)) {
            redirectAttributes.addFlashAttribute("message", "评论已删除");
        } else {
            redirectAttributes.addFlashAttribute("error", "删除失败");
        }
        
        return "redirect:/user/comment/list";
    }
}

