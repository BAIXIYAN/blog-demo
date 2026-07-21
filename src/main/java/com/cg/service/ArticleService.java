package com.cg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cg.entity.Article;
import com.cg.entity.vo.ArticleVO;

import java.util.List;

/**
 * 文章服务接口
 */
public interface ArticleService extends IService<Article> {

    /**
     * 发布文章
     * @param article 文章信息
     * @return 发布结果
     */
    boolean publishArticle(Article article);

    /**
     * 审核文章
     * @param articleId 文章ID
     * @param status 审核状态（1-通过，2-拒绝）
     * @return 审核结果
     */
    boolean reviewArticle(Long articleId, Integer status);

    /**
     * 增加文章浏览量
     * @param articleId 文章ID
     */
    void increaseViewCount(Long articleId);

    /**
     * 获取文章列表（带分类和作者信息）
     * @return 文章列表
     */
    List<ArticleVO> getArticleList();

    /**
     * 根据分类获取文章
     * @param categoryId 分类ID
     * @return 文章列表
     */
    List<ArticleVO> getArticlesByCategory(Long categoryId);
    
    /**
     * 根据ID获取文章详情（转换为ArticleVO）
     * @param id 文章ID
     * @return 文章详情
     */
    ArticleVO getArticleVOById(Long id);
    
    /**
     * 根据ID获取文章详情（转换为ArticleVO，带用户ID用于判断是否已点赞）
     * @param id 文章ID
     * @param userId 用户ID（可为null）
     * @return 文章详情
     */
    ArticleVO getArticleVOById(Long id, Long userId);
    
    /**
     * 获取用户自己的文章列表
     * @param userId 用户ID
     * @return 文章列表
     */
    List<ArticleVO> getArticlesByUser(Long userId);
    
    /**
     * 获取待审核文章列表
     * @return 文章列表
     */
    List<ArticleVO> getPendingArticles();
    
    /**
     * 获取所有文章列表（管理员用，包括所有状态）
     * @return 文章列表
     */
    List<ArticleVO> getAllArticles();
    
    /**
     * 搜索文章（根据关键词搜索标题、摘要、内容）
     * @param keyword 搜索关键词
     * @return 文章列表
     */
    List<ArticleVO> searchArticles(String keyword);
    
    /**
     * 分页获取文章列表（带分类和作者信息）
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @param sortField 排序字段：publishTime-发布时间, viewCount-浏览量
     * @param sortOrder 排序方式：desc-降序, asc-升序
     * @return 分页结果
     */
    IPage<ArticleVO> getArticleListPage(int page, int size, String sortField, String sortOrder);

    /**
     * 分页获取分类文章列表
     * @param categoryId 分类ID
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @param sortField 排序字段：publishTime-发布时间, viewCount-浏览量
     * @param sortOrder 排序方式：desc-降序, asc-升序
     * @return 分页结果
     */
    IPage<ArticleVO> getArticlesByCategoryPage(Long categoryId, int page, int size, String sortField, String sortOrder);

    /**
     * 分页搜索文章
     * @param keyword 搜索关键词
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @param sortField 排序字段：publishTime-发布时间, viewCount-浏览量
     * @param sortOrder 排序方式：desc-降序, asc-升序
     * @return 分页结果
     */
    IPage<ArticleVO> searchArticlesPage(String keyword, int page, int size, String sortField, String sortOrder);
}