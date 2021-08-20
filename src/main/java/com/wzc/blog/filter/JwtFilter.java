package com.wzc.blog.filter;

import com.wzc.blog.pojo.AuthUser;
import com.wzc.blog.util.JwtProperties;
import com.wzc.blog.pojo.User;
import com.wzc.blog.service.UserService;
import com.wzc.blog.util.CookieUtils;
import com.wzc.blog.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class JwtFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(SecurityContextHolder.getContext().getAuthentication()==null){
            String token = CookieUtils.getCookieValue(httpServletRequest, jwtProperties.getCookieName());
            if(token!=null)
            {
                try {
                    JwtUtils.validateToken(token,jwtProperties.getPublicKey());
                    logger.info("正在使用token验证用户");
                    User user =  JwtUtils.getInfoFromToken(token,jwtProperties.getPublicKey());
                    List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getType()+"");
                    AuthUser authUser = new AuthUser(user.getUsername(),user.getNickname(),authorities);
                    Authentication auth = new UsernamePasswordAuthenticationToken(authUser,"",authUser.getAuthorities());
                    if (auth != null) {
                        logger.info("验证成功,正在登录");
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        httpServletRequest.getSession().setAttribute("user",user);
                        logger.info("登录成功,刷新token");
                        String newToken = JwtUtils.generateToken(user,jwtProperties.getPrivateKey(),jwtProperties.getCookieMaxAge());
                        CookieUtils.setCookie(httpServletRequest, httpServletResponse, jwtProperties.getCookieName(),newToken, jwtProperties.getCookieMaxAge(), "utf8");
                        logger.info("token刷新成功,跳转主页面");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("验证失败,请检查token是否正确");
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
