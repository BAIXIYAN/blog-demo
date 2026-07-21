package com.cg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cg.entity.Article;
import com.cg.entity.Comment;
import com.cg.entity.User;
import com.cg.entity.vo.CommentVO;
import com.cg.mapper.CommentMapper;
import com.cg.service.ArticleService;
import com.cg.service.CommentService;
import com.cg.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ArticleService articleService;
    
    @Override
    public boolean addComment(Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        return save(comment);
    }
    
    @Override
    public List<CommentVO> getCommentsByArticle(Long articleId) {
        // 获取该文章的所有评论（包括顶级评论和回复）
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId)
                   .orderByAsc("create_time");  // 按时间正序，先显示早的评论
        
        List<Comment> allComments = list(queryWrapper);
        
        // 转换为VO列表
        List<CommentVO> allCommentVOs = convertToVOList(allComments);
        
        // 构建嵌套结构以设置回复对象信息
        List<CommentVO> treeComments = buildCommentTree(allCommentVOs);
        
        // 扁平化评论树，按时间排序
        List<CommentVO> flatComments = flattenComments(treeComments);
        flatComments.sort((a, b) -> a.getCreateTime().compareTo(b.getCreateTime()));
        
        return flatComments;
    }
    
    /**
     * 构建评论树结构
     */
    private List<CommentVO> buildCommentTree(List<CommentVO> allComments) {
        // 存储顶级评论
        List<CommentVO> rootComments = new ArrayList<>();
        // 存储所有评论的映射（id -> CommentVO）
        java.util.Map<Long, CommentVO> commentMap = new java.util.HashMap<>();
        
        // 第一遍遍历：建立映射，并找出顶级评论
        for (CommentVO comment : allComments) {
            commentMap.put(comment.getId(), comment);
            if (comment.getParentId() == null || comment.getParentId() == 0) {
                rootComments.add(comment);
            }
        }
        
        // 第二遍遍历：构建父子关系，并设置回复对象信息
        for (CommentVO comment : allComments) {
            if (comment.getParentId() != null && comment.getParentId() != 0) {
                // 这是回复评论
                CommentVO parentComment = commentMap.get(comment.getParentId());
                if (parentComment != null) {
                    // 添加到父评论的子评论列表
                    parentComment.getChildren().add(comment);
                    
                    // 设置被回复的用户信息
                    comment.setReplyToUserId(parentComment.getUserId());
                    comment.setReplyToUsername(parentComment.getUsername());
                    comment.setReplyToNickname(parentComment.getNickname());
                }
            }
        }
        
        // 对顶级评论按时间倒序排序
        rootComments.sort((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()));
        
        // 对每个评论的子评论按时间正序排序（早的在前）
        for (CommentVO rootComment : rootComments) {
            sortChildren(rootComment);
        }
        
        return rootComments;
    }
    
    /**
     * 递归排序子评论
     */
    private void sortChildren(CommentVO comment) {
        if (comment.getChildren() != null && !comment.getChildren().isEmpty()) {
            comment.getChildren().sort((a, b) -> a.getCreateTime().compareTo(b.getCreateTime()));
            for (CommentVO child : comment.getChildren()) {
                sortChildren(child);
            }
        }
    }
    
    /**
     * 扁平化评论树结构，将嵌套的评论展平为列表
     */
    private List<CommentVO> flattenComments(List<CommentVO> comments) {
        List<CommentVO> result = new ArrayList<>();
        for (CommentVO comment : comments) {
            result.add(comment);
            if (comment.getChildren() != null && !comment.getChildren().isEmpty()) {
                result.addAll(flattenComments(comment.getChildren()));
            }
        }
        return result;
    }
    
    @Override
    public List<CommentVO> getCommentsByUser(Long userId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
        
        List<Comment> comments = list(queryWrapper);
        return convertToVOList(comments);
    }
    
    @Override
    public List<CommentVO> getAllComments() {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        
        List<Comment> comments = list(queryWrapper);
        return convertToVOList(comments);
    }
    
    /**
     * 将Comment列表转换为CommentVO列表
     */
    private List<CommentVO> convertToVOList(List<Comment> comments) {
        List<CommentVO> voList = new ArrayList<>();
        
        for (Comment comment : comments) {
            CommentVO vo = new CommentVO();
            BeanUtils.copyProperties(comment, vo);
            
            // 获取用户信息
            User user = userService.getById(comment.getUserId());
            if (user != null) {
                vo.setUsername(user.getUsername());
                vo.setNickname(user.getNickname());
                vo.setAvatar(user.getAvatar());
            }
            
            // 获取文章信息
            Article article = articleService.getById(comment.getArticleId());
            if (article != null) {
                vo.setArticleTitle(article.getTitle());
            }
            
            voList.add(vo);
        }
        
        return voList;
    }
}