package com.itheima.Reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.Reggie.entity.Employee;
import com.itheima.Reggie.mapper.EmployeeMapper;
import com.itheima.Reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

// service是bean的一种，
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {
}
