package com.itheima.Reggie.dto;

import com.itheima.Reggie.entity.Setmeal;
import com.itheima.Reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
