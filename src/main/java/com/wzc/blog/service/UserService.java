package com.wzc.blog.service;

import com.wzc.blog.mapper.UserMapper;
import com.wzc.blog.pojo.JwtProperties;
import com.wzc.blog.pojo.User;
import com.wzc.blog.util.CookieUtils;
import com.wzc.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public User checkUser(String username, String password) {
        User temp = new User();
        temp.setUsername(username);
        temp.setPassword(MD5Utils.code(password));
        return userMapper.selectOne(temp);
    }

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
}
