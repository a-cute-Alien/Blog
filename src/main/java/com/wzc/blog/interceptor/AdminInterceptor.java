package com.wzc.blog.interceptor;

import com.wzc.blog.pojo.JwtProperties;
import com.wzc.blog.util.CookieUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        if(StringUtil.isEmpty(token))
        {
            response.sendRedirect("/admin");
            return  false;
        }
        return true;
    }
}
