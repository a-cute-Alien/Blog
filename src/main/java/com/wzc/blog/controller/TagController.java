package com.wzc.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzc.blog.pojo.BlogInfo;
import com.wzc.blog.pojo.Tag;
import com.wzc.blog.pojo.Type;
import com.wzc.blog.service.BlogService;
import com.wzc.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TagController {

    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;

    @GetMapping("tags/{id}")
    public String getAllBlogInfoByTag(@PathVariable Long id, @RequestParam(required = false,defaultValue = "1",value = "pageNum")int pagenum, Model model){

        List<Tag> allTag = tagService.getAllTag();
        //-1从导航点过来的
        if (id == -1){
            id = allTag.get(0).getId();
        }
        PageHelper.startPage(pagenum, 4);  //开启分页
        List<BlogInfo> allBlogInfo = blogService.getBlogInfoByTagId(id);
        PageInfo<BlogInfo> pageInfo = new PageInfo<>(allBlogInfo);
        model.addAttribute("tags", allTag);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("activeTagId", id);
        return "tags";
    }

}
