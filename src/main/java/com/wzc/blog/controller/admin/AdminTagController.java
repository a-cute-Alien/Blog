package com.wzc.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzc.blog.pojo.Tag;
import com.wzc.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminTagController {

    @Autowired
    private TagService tagService;

    @GetMapping("tags")
    public String tags(@RequestParam(required = false,defaultValue = "1",value = "pageNum")int pagenum, Model model){
        PageHelper.startPage(pagenum, 4);
        List<Tag> allTag = tagService.getAllTag();
        PageInfo<Tag> pageInfo = new PageInfo<>(allTag);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/tags";
    }

    @GetMapping("tag/{id}")
    public String toEditTag(@PathVariable Long id, Model model){
        Tag tag = tagService.getTagById(id);
        if(tag!=null)
            model.addAttribute("tag",tag); //保存修改前的记录
        else
            model.addAttribute("tag",new Tag()); //添加
        return "admin/tags-input";
    }

    @PostMapping("tag/add")
    public String addTag(Tag tag, RedirectAttributes attributes){   //新增
        Tag t = tagService.getTagByName(tag.getName());
        if(t != null){
            attributes.addFlashAttribute("msg", "不能添加重复的标签");
            return "redirect:/admin/tag/add";
        }else {
            if(tagService.addTag(tag))
                attributes.addFlashAttribute("msg", "添加成功");
        }
        return "redirect:/admin/tags";   //不能直接跳转到tags页面，否则不会显示tag数据(没经过tags方法)
    }

    @PostMapping("tag/edit")
    public String editTag(Tag tag, RedirectAttributes attributes){  //修改
        Tag t = tagService.getTagByName(tag.getName());
        if(t != null){
            attributes.addFlashAttribute("msg", "不能添加重复的标签");
            return "redirect:/admin/tag/edit";
        }else {
            if(tagService.updateTag(tag))
                attributes.addFlashAttribute("msg", "修改成功");
        }
        return "redirect:/admin/tags";   //不能直接跳转到tags页面，否则不会显示tag数据(没经过tags方法)
    }
    @GetMapping("tag/delete/{id}")
    public String deleteTag(@PathVariable Long id, RedirectAttributes attributes){
        if(tagService.deleteTag(id))
            attributes.addFlashAttribute("msg", "删除成功");
        return "redirect:/admin/tags";
    }
}
