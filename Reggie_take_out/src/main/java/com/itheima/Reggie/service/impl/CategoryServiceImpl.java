package com.itheima.Reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.Reggie.common.CustomException;
import com.itheima.Reggie.entity.Category;
import com.itheima.Reggie.entity.Dish;
import com.itheima.Reggie.entity.Setmeal;
import com.itheima.Reggie.mapper.CategoryMapper;
import com.itheima.Reggie.service.CategoryService;
import com.itheima.Reggie.service.DishService;
import com.itheima.Reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> SetmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        SetmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);

        if (dishService.count(dishLambdaQueryWrapper) > 0) {
            throw new CustomException("此分类关联了商品，无法删除");
        }

        if (setmealService.count(SetmealLambdaQueryWrapper) > 0) {
            throw new CustomException("此分类关联了套餐，无法删除");
        }

        super.removeById(id);
    }
}
