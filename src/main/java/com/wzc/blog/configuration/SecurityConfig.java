package com.wzc.blog.configuration;

import com.wzc.blog.filter.UserAuthenticationProcessingFilter;
import com.wzc.blog.filter.JwtFilter;
import com.wzc.blog.filter.UserAuthenticationConfig.UserAuthenticationManager;
import com.wzc.blog.util.JwtProperties;
import com.wzc.blog.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserAuthenticationManager userAuthenticationManager;

    @Autowired
    private JwtProperties jwtProperties;

    @Bean
    public UserAuthenticationProcessingFilter getAuthenticationProcessingFilter(){
        return new UserAuthenticationProcessingFilter(userAuthenticationManager);
    }
    @Bean
    public JwtFilter getJwtFilter(){
        return new JwtFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //jwt过滤器
        http.addFilterBefore(getJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        //自定义登录
        http.addFilterAt(getAuthenticationProcessingFilter(),UsernamePasswordAuthenticationFilter.class);
        //权限控制
        http.authorizeRequests()
                .antMatchers("/admin/login","/admin/nonuser").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("1")
                .antMatchers("/admin/**").authenticated();

        //登录配置
        http.formLogin()
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login");
        //注销配置
        http.logout()
                .logoutUrl("/admin/logout").logoutSuccessHandler(new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                CookieUtils.deleteCookie(httpServletRequest,httpServletResponse,jwtProperties.getCookieName());
                httpServletResponse.sendRedirect("/");
            }
        });

        //跨域关闭
        http.csrf().disable();

        //无权访问
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                httpServletResponse.sendRedirect("/error/403");
            }
        });


    }

}
