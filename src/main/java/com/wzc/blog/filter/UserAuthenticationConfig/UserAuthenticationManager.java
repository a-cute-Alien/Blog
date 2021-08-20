package com.wzc.blog.filter.UserAuthenticationConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication result = userAuthenticationProvider.authenticate(authentication);
        if (Objects.nonNull(result)) {
            return result;
        }
        throw new ProviderNotFoundException("Authentication failed!");
    }
}
