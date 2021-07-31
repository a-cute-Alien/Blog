package com.wzc.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzc.blog.pojo.*;
import com.wzc.blog.service.BlogService;
import com.wzc.blog.service.TagService;
import com.wzc.blog.service.TypeService;
import com.wzc.blog.service.UserService;
import com.wzc.blog.util.CookieUtils;
import com.wzc.blog.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminUserController {


    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @GetMapping("")
    public String toLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        if(!StringUtil.isEmpty(token)) {
            // 从token中解析token信息
            User userInfo = null;
            try {
                userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
                // 解析成功要重新刷新token
                token = JwtUtils.generateToken(userInfo, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());
                // 更新cookie中的token
                CookieUtils.setCookie(request, response, this.jwtProperties.getCookieName(), token, this.jwtProperties.getCookieMaxAge());
                session.setAttribute("user", userInfo);
                return "admin/index";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "admin/login";

    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response
                        ) throws Exception {
       if(!StringUtil.isEmpty(username)&&!StringUtil.isEmpty(password)){
            User user = userService.checkUser(username, password);
            if(user != null){
                user.setPassword(null);
                session.setAttribute("user", user);
                //JWT
                String newToken = JwtUtils.generateToken(user,jwtProperties.getPrivateKey(), jwtProperties.getExpire());
                CookieUtils.setCookie(request, response, jwtProperties.getCookieName(),newToken, jwtProperties.getCookieMaxAge(), "utf8");
                return "admin/index";
            }
        }
       model.addAttribute("msg", "用户名或密码错误");
       return "redirect:/admin";
    }

}
