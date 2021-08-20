package com.wzc.blog.filter;

import com.wzc.blog.pojo.AuthUser;
import com.wzc.blog.util.JwtProperties;
import com.wzc.blog.pojo.User;
import com.wzc.blog.util.CookieUtils;
import com.wzc.blog.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class UserAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JwtProperties jwtProperties;

    public UserAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/admin/login", "POST"));
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler(){
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                httpServletResponse.sendRedirect("/admin/index");
            }
        });
        this.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                httpServletResponse.sendRedirect("/admin/nonuser");
            }
        });
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
        String userName;
        String password;
        List<GrantedAuthority> authorities;
        Authentication result=null;
        try {
            logger.info("正在通过用户名、密码验证登录");
            userName = (String) httpServletRequest.getParameter("username");
            password = (String) httpServletRequest.getParameter("password");
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
            usernamePasswordAuthenticationToken.setDetails(authenticationDetailsSource.buildDetails(httpServletRequest));
            result = this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
            if(result!=null){
                AuthUser resultUser = (AuthUser) result.getPrincipal();
                User userInfo = new User();userInfo.setId(resultUser.getId());
                userInfo.setUsername(resultUser.getUsername());
                userInfo.setNickname(resultUser.getNickName());
                userInfo.setAvatar(resultUser.getAvatar());
                String newToken = JwtUtils.generateToken(userInfo,jwtProperties.getPrivateKey(), jwtProperties.getExpire());
                CookieUtils.setCookie(httpServletRequest, httpServletResponse, jwtProperties.getCookieName(),newToken, jwtProperties.getCookieMaxAge(), "utf8");
                httpServletRequest.getSession().setAttribute("user",userInfo);
                logger.info("验证成功");
            }
        } catch (Exception e) {
            logger.info("验证登失败");
            throw new AuthenticationServiceException(e.getMessage());
        }
        return result;
    }
}
