package com.itheima.Reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.Reggie.common.BaseContext;
import com.itheima.Reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 所有的请求都拦截
// threadlocal为每一个线程提供一个单独的存储空间，线程外不能访问。
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LogicCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        log.info("拦截到请求：{}",request);

        // 不需要处理的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
//                "/employee/page",
                "/backend/**",
                "/front/**"
        };

        boolean check = check(urls, requestURI);

        // 检查已经登录，放行
        if(check) {
            log.info("本次请求不需要处理：{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        if(request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");

            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return ;
        }

        // 未登录，返回未登录结果
        log.info("用户未登录：{}",requestURI);
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return ;

    }

    //路径匹配，检查这次请求是否需要放行
    public boolean check(String[] urls,String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match) {
                return true;
            }
        }
        return false;
    }
}
