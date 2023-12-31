package com.itheima.Reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.Reggie.dto.DishDto;
import com.itheima.Reggie.entity.Dish;
import com.itheima.Reggie.entity.DishFlavor;

public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入菜品对应的口味数据，需要操作两张表
    public void saveWithFlavor(DishDto dishDto);

    // 根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavour(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
