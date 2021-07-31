package com.wzc.blog.controller;

import com.wzc.blog.pojo.Blog;
import com.wzc.blog.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class UserController {


    @GetMapping()
    public String queryUser(User user){
        return "";
    }


}
