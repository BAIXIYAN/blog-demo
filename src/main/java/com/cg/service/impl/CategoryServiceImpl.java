package com.cg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cg.entity.Category;
import com.cg.mapper.CategoryMapper;
import com.cg.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章分类服务实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    
    @Override
    public boolean existsByName(String name) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return count(queryWrapper) > 0;
    }
    
    @Override
    public List<Category> listOrderBySort() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        return list(queryWrapper);
    }
}