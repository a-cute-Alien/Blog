package com.wzc.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzc.blog.pojo.BlogInfo;
import com.wzc.blog.pojo.Type;
import com.wzc.blog.service.BlogService;
import com.wzc.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TypeController {
    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String getBlogByType(@PathVariable Long id, @RequestParam(required = false,defaultValue = "1",value = "pageNum")int pagenum,
                        Model model){

        List<Type> allType = typeService.getAllType();
        //-1从导航点过来的
        if (id == -1){
            id = allType.get(0).getId();
        }
        PageHelper.startPage(pagenum, 4);
        List<BlogInfo> allBlogInfo = blogService.getBlogInfoByTypeId(id);
        PageInfo<BlogInfo> pageInfo = new PageInfo<>(allBlogInfo);
        model.addAttribute("types", allType);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("activeTypeId", id);
        return "types";
    }



}
