package com.cg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cg.entity.Comment;
import com.cg.entity.vo.CommentVO;

import java.util.List;

/**
 * 评论服务接口
 */
public interface CommentService extends IService<Comment> {

    /**
     * 添加评论
     * @param comment 评论信息
     * @return 添加结果
     */
    boolean addComment(Comment comment);

    /**
     * 获取文章评论列表
     * @param articleId 文章ID
     * @return 评论列表
     */
    List<CommentVO> getCommentsByArticle(Long articleId);

    /**
     * 获取用户评论列表
     * @param userId 用户ID
     * @return 评论列表
     */
    List<CommentVO> getCommentsByUser(Long userId);
    
    /**
     * 获取所有评论列表（管理员用）
     * @return 评论列表
     */
    List<CommentVO> getAllComments();
}