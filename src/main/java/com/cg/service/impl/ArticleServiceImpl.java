package com.cg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cg.entity.Article;
import com.cg.entity.Category;
import com.cg.entity.User;
import com.cg.entity.vo.ArticleVO;
import com.cg.mapper.ArticleMapper;
import com.cg.service.ArticleService;
import com.cg.service.CategoryService;
import com.cg.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章服务实现类
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private com.cg.service.ArticleLikeService articleLikeService;
    
    @Autowired
    private com.cg.service.ArticleFavoriteService articleFavoriteService;
    
    @Override
    public boolean publishArticle(Article article) {
        // 设置默认值
        article.setStatus(0); // 待审核
        if (article.getViewCount() == null) {
            article.setViewCount(0);
        }
        if (article.getCreateTime() == null) {
            article.setCreateTime(LocalDateTime.now());
        }
        article.setUpdateTime(LocalDateTime.now());
        
        return save(article);
    }
    
    @Override
    public boolean reviewArticle(Long articleId, Integer status) {
        Article article = getById(articleId);
        if (article == null) {
            return false;
        }
        
        article.setStatus(status);
        if (status == 1) { // 审核通过
            article.setPublishTime(LocalDateTime.now());
        }
        article.setUpdateTime(LocalDateTime.now());
        
        return updateById(article);
    }
    
    @Override
    public void increaseViewCount(Long articleId) {
        Article article = getById(articleId);
        if (article != null) {
            int currentCount = article.getViewCount() != null ? article.getViewCount() : 0;
            article.setViewCount(currentCount + 1);
            updateById(article);
        }
    }
    
    @Override
    public List<ArticleVO> getArticleList() {
        // 查询已发布的文章
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1)  // 已发布
                   .orderByDesc("publish_time");
        
        List<Article> articles = list(queryWrapper);
        return convertToVOList(articles);
    }
    
    @Override
    public List<ArticleVO> getArticlesByCategory(Long categoryId) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId)
                   .eq("status", 1)  // 已发布
                   .orderByDesc("publish_time");
        
        List<Article> articles = list(queryWrapper);
        return convertToVOList(articles);
    }
    
    /**
     * 将Article列表转换为ArticleVO列表
     */
    private List<ArticleVO> convertToVOList(List<Article> articles) {
        return convertToVOList(articles, null);
    }
    
    /**
     * 将Article列表转换为ArticleVO列表（带用户ID，用于判断是否已点赞）
     */
    private List<ArticleVO> convertToVOList(List<Article> articles, Long userId) {
        List<ArticleVO> voList = new ArrayList<>();
        
        for (Article article : articles) {
            ArticleVO vo = new ArticleVO();
            BeanUtils.copyProperties(article, vo);
            
            // 获取分类信息
            Category category = categoryService.getById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
            
            // 获取作者信息
            User author = userService.getById(article.getUserId());
            if (author != null) {
                vo.setAuthorName(author.getNickname());
                vo.setAuthorAvatar(author.getAvatar());
            }
            
            // 获取点赞信息
            vo.setLikeCount(articleLikeService.getLikeCount(article.getId()));
            if (userId != null) {
                vo.setIsLiked(articleLikeService.isLiked(article.getId(), userId));
            } else {
                vo.setIsLiked(false);
            }
            
            // 获取收藏信息
            vo.setFavoriteCount(articleFavoriteService.getFavoriteCount(article.getId()));
            if (userId != null) {
                vo.setIsFavorited(articleFavoriteService.isFavorited(article.getId(), userId));
            } else {
                vo.setIsFavorited(false);
            }
            
            voList.add(vo);
        }
        
        return voList;
    }
    
    @Override
    public ArticleVO getArticleVOById(Long id) {
        return getArticleVOById(id, null);
    }
    
    /**
     * 根据ID获取文章详情（转换为ArticleVO，带用户ID用于判断是否已点赞）
     */
    public ArticleVO getArticleVOById(Long id, Long userId) {
        Article article = getById(id);
        if (article == null) {
            return null;
        }
        
        ArticleVO vo = new ArticleVO();
        BeanUtils.copyProperties(article, vo);
        
        // 获取分类信息
        Category category = categoryService.getById(article.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }
        
        // 获取作者信息
        User author = userService.getById(article.getUserId());
        if (author != null) {
            vo.setAuthorName(author.getNickname());
            vo.setAuthorAvatar(author.getAvatar());
        }
        
        // 获取点赞信息
        vo.setLikeCount(articleLikeService.getLikeCount(article.getId()));
        if (userId != null) {
            vo.setIsLiked(articleLikeService.isLiked(article.getId(), userId));
        } else {
            vo.setIsLiked(false);
        }
        
        // 获取收藏信息
        vo.setFavoriteCount(articleFavoriteService.getFavoriteCount(article.getId()));
        if (userId != null) {
            vo.setIsFavorited(articleFavoriteService.isFavorited(article.getId(), userId));
        } else {
            vo.setIsFavorited(false);
        }
        
        return vo;
    }
    
    @Override
    public List<ArticleVO> getArticlesByUser(Long userId) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
        
        List<Article> articles = list(queryWrapper);
        return convertToVOList(articles);
    }
    
    @Override
    public List<ArticleVO> getPendingArticles() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0)  // 待审核
                   .orderByDesc("create_time");
        
        List<Article> articles = list(queryWrapper);
        return convertToVOList(articles);
    }
    
    @Override
    public List<ArticleVO> getAllArticles() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        
        List<Article> articles = list(queryWrapper);
        return convertToVOList(articles);
    }
    
    @Override
    public List<ArticleVO> searchArticles(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getArticleList(); // 如果关键词为空，返回所有已发布文章
        }
        
        // 搜索已发布的文章，在标题、摘要、内容中搜索关键词
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1)  // 已发布
                   .and(wrapper -> wrapper
                       .like("title", keyword.trim())
                       .or()
                       .like("summary", keyword.trim())
                       .or()
                       .like("content", keyword.trim())
                   )
                   .orderByDesc("publish_time");
        
        List<Article> articles = list(queryWrapper);
        return convertToVOList(articles);
    }
    
    @Override
    public IPage<ArticleVO> getArticleListPage(int page, int size, String sortField, String sortOrder) {
        // 创建分页对象
        Page<Article> pageParam = new Page<>(page, size);
        
        // 查询已发布的文章
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);  // 已发布
        
        // 应用排序
        applySort(queryWrapper, sortField, sortOrder);
        
        // 执行分页查询
        IPage<Article> articlePage = page(pageParam, queryWrapper);
        
        // 转换为ArticleVO分页结果
        return convertToVOPage(articlePage);
    }

    @Override
    public IPage<ArticleVO> getArticlesByCategoryPage(Long categoryId, int page, int size, String sortField, String sortOrder) {
        // 创建分页对象
        Page<Article> pageParam = new Page<>(page, size);
        
        // 查询分类文章
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId)
                   .eq("status", 1);  // 已发布
        
        // 应用排序
        applySort(queryWrapper, sortField, sortOrder);
        
        // 执行分页查询
        IPage<Article> articlePage = page(pageParam, queryWrapper);
        
        // 转换为ArticleVO分页结果
        return convertToVOPage(articlePage);
    }

    @Override
    public IPage<ArticleVO> searchArticlesPage(String keyword, int page, int size, String sortField, String sortOrder) {
        // 创建分页对象
        Page<Article> pageParam = new Page<>(page, size);
        
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);  // 已发布
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 搜索已发布的文章，在标题、摘要、内容中搜索关键词
            queryWrapper.and(wrapper -> wrapper
                .like("title", keyword.trim())
                .or()
                .like("summary", keyword.trim())
                .or()
                .like("content", keyword.trim())
            );
        }
        
        // 应用排序
        applySort(queryWrapper, sortField, sortOrder);
        
        // 执行分页查询
        IPage<Article> articlePage = page(pageParam, queryWrapper);
        
        // 转换为ArticleVO分页结果
        return convertToVOPage(articlePage);
    }
    
    /**
     * 应用排序规则
     * @param queryWrapper 查询包装器
     * @param sortField 排序字段：publishTime-发布时间, viewCount-浏览量
     * @param sortOrder 排序方式：desc-降序, asc-升序
     */
    private void applySort(QueryWrapper<Article> queryWrapper, String sortField, String sortOrder) {
        // 默认按发布时间降序
        if (sortField == null || sortField.trim().isEmpty()) {
            sortField = "publishTime";
        }
        if (sortOrder == null || sortOrder.trim().isEmpty()) {
            sortOrder = "desc";
        }
        
        // 转换为数据库字段名
        String dbField;
        if ("viewCount".equalsIgnoreCase(sortField)) {
            dbField = "view_count";
        } else {
            dbField = "publish_time";  // 默认按发布时间
        }
        
        // 应用排序
        if ("asc".equalsIgnoreCase(sortOrder)) {
            queryWrapper.orderByAsc(dbField);
        } else {
            queryWrapper.orderByDesc(dbField);
        }
    }
    
    /**
     * 将Article分页结果转换为ArticleVO分页结果
     */
    private IPage<ArticleVO> convertToVOPage(IPage<Article> articlePage) {
        // 创建ArticleVO分页对象
        Page<ArticleVO> voPage = new Page<>(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());
        
        // 转换列表
        List<ArticleVO> voList = convertToVOList(articlePage.getRecords());
        voPage.setRecords(voList);
        
        return voPage;
    }
}