package com.wzc.blog.service;

import com.wzc.blog.mapper.UserMapper;
import com.wzc.blog.pojo.AuthUser;
import com.wzc.blog.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService  implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User getUserById(Long id){
        return  userMapper.selectByPrimaryKey(id);
    }

    public List<User> getAllUsers(){
        return userMapper.selectAll();
    }

    public boolean addUser(User user){
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return userMapper.insert(user) > 0;
    }

    public boolean deleteUser(Long id){
        return  userMapper.deleteByPrimaryKey(id) > 0;
    }
    public boolean updateUser(User user){
        return userMapper.updateByPrimaryKey(user)>0;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User temp = new User();
        temp.setUsername(userName);
        String password = null;
        List<GrantedAuthority> authorities = null;
        User result = userMapper.selectOne(temp);
        AuthUser authUser=null;
        if(result!=null){
            password = result.getPassword();
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(result.getType()+"");
            authUser = new AuthUser(userName,password,authorities);
            authUser.setAvatar(result.getAvatar());
            authUser.setNickName(result.getNickname());
            authUser.setId(result.getId());
        }
        return authUser;
    }
}
