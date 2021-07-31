package com.wzc.blog.controller;

import com.wzc.blog.pojo.Blog;
import com.wzc.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("archives")
public class TimeController {
    @Autowired
    private BlogService blogService;
    @GetMapping("")
    public String archives(Model model) {
        Map<String, List<Blog>> blogMap = blogService.getBlogsByUpdateTime();
        int count = 0;
        for( List<Blog> blogList:blogMap.values()){
            count+=blogList.size();
        }
        model.addAttribute("archiveMap", blogMap);
        model.addAttribute("blogCount", count);
        return "archives";
    }
}
