package com.itheima.Reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.Reggie.entity.Employee;

//依赖注入和解耦: 在面向接口编程中，往往使用依赖注入的方式将实现类注入到其他组件中。这种方式降低了组件之间的直接依赖，提高了代码的松耦合性，使得系统更容易进行单元测试和模块替换。
public interface EmployeeService extends IService<Employee> {
}
