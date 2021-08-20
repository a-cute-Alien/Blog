package com.wzc.blog.filter.UserAuthenticationConfig;

import com.wzc.blog.pojo.AuthUser;
import com.wzc.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        AuthUser authUser = (AuthUser) userService.loadUserByUsername(userName);
        if(bCryptPasswordEncoder.matches(password,authUser.getPassword())==false){
            throw new BadCredentialsException("密码错误！");
        }
        return new UsernamePasswordAuthenticationToken(authUser,authUser.getPassword(),authUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
