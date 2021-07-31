package com.wzc.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzc.blog.pojo.Blog;
import com.wzc.blog.pojo.BlogInfo;
import com.wzc.blog.pojo.Tag;
import com.wzc.blog.pojo.User;
import com.wzc.blog.service.BlogService;
import com.wzc.blog.service.TagService;
import com.wzc.blog.service.TypeService;
import com.wzc.blog.util.MarkdownUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller

public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("blog/{id}")
    public String getBlogInfoById(@PathVariable("id") Long id, Model model) throws NotFoundException {
        BlogInfo blogInfo = blogService.getBlogInfoById(id);
        if (blogInfo == null) {
            throw new NotFoundException("该博客不存在");
        }
        String content = blogInfo.getBlog().getContent();
        blogInfo.getBlog().setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        model.addAttribute("blogInfo", blogInfo);
        return "blog";
    }


    @PostMapping("blog/search")
    public String searchBlogs(@RequestParam String query, @RequestParam(required = false,defaultValue = "1",value = "pageNum")int pageNum, Model model){
        PageHelper.startPage(pageNum,4);
        List<BlogInfo> blogInfo = blogService.getBlogInfoByKeyWord(query);
        PageInfo pageInfo = new PageInfo(blogInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("query", query);
        return "search";
    }

    public void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.getAllType());
        model.addAttribute("tags", tagService.getAllTag());
    }


}
