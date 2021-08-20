package com.wzc.blog.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzc.blog.pojo.Blog;
import com.wzc.blog.pojo.Tag;
import com.wzc.blog.pojo.Type;
import com.wzc.blog.service.BlogService;
import com.wzc.blog.service.TagService;
import com.wzc.blog.service.TypeService;
import com.wzc.blog.service.UserService;
import com.wzc.blog.pojo.vo.BlogInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(tags = "主页控制器")
@Controller
public class MainController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @ApiOperation("查询并显示主页")
    @RequestMapping("/")
    public String root(@RequestParam(required = false,defaultValue = "1",value = "pageNum")int pagenum, Model model){
        PageHelper.startPage(pagenum, 4);
        List<BlogInfo> allBlogInfo = blogService.getAllBlogInfo();
        List<Type> allType = typeService.getAllType();
        List<Tag> allTag = tagService.getAllTag();
        List<Blog> recommendBlog = blogService.getBlogsByRecommend();
        PageInfo pageInfo = new PageInfo(allBlogInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("types", allType);
        model.addAttribute("tags", allTag);
        model.addAttribute("recommendBlogs",recommendBlog);
        return "index";
    }
}
