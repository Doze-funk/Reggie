package com.itheima.Reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.Reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);

}
