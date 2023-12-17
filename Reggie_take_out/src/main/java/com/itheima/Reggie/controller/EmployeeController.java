package com.itheima.Reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.Reggie.common.R;
import com.itheima.Reggie.entity.Employee;
import com.itheima.Reggie.service.EmployeeService;
import jdk.nashorn.internal.ir.ReturnNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login") // 前端发送post请求
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        String password = employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());

        // 根据用户名查询数据库
        // 包装查询对象
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 封装查询条件
        queryWrapper.eq(Employee::getUsername,employee.getUsername()); //lamda匿名函数用法
        // 获取唯一值
        // 返回符合条件的一条 Employee 记录。如果查询结果有多条符合条件的记录，它会抛出异常；如果没有符合条件的记录，返回 null。
        Employee emp = employeeService.getOne(queryWrapper);

        if (emp == null) {
            return R.error("登陆失败");
        }

        if(!emp.getPassword().equals(password)) {
            return R.error("登陆失败");
        }

        if(emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 清除用户信息,确保每一次登录都是一次全新的会话。
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request ,@RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}",employee.toString());

        // 设置初始密码，默认123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long employeeId = (Long)request.getSession().getAttribute("employee");
//        employee.setCreateUser(employeeId);
//        employee.setUpdateUser(employeeId);
        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page , int pageSize ,String name) {
        log.info("page = {} ,pageSize = {} ,name = {}" ,page ,pageSize ,name);

        Page pageInfo = new Page(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name) , Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo , queryWrapper);

        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request ,@RequestBody Employee employee) {
        log.info(employee.toString());

        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
