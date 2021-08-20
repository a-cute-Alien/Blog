package com.wzc.blog.controller;

import com.wzc.blog.pojo.Blog;
import com.wzc.blog.pojo.User;
import com.wzc.blog.service.IPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

    @GetMapping()
    public String queryUser(User user){
        return "";
    }

}
