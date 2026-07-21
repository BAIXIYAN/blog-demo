package com.cg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cg.entity.Category;

import java.util.List;

/**
 * 文章分类服务接口
 */
public interface CategoryService extends IService<Category> {

    /**
     * 检查分类名称是否存在
     * @param name 分类名称
     * @return 是否存在
     */
    boolean existsByName(String name);
    
    /**
     * 获取所有分类，按排序字段排序
     * @return 分类列表
     */
    List<Category> listOrderBySort();
}