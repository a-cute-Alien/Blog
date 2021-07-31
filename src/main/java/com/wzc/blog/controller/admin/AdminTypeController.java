package com.wzc.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzc.blog.pojo.Type;
import com.wzc.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminTypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String getAllType(@RequestParam(required = false,defaultValue = "1",value = "pageNum")int pagenum, Model model){
        PageHelper.startPage(pagenum, 8);
        List<Type> allType = typeService.getAllType();
        PageInfo<Type> pageInfo = new PageInfo<>(allType);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/types";
    }

    @GetMapping("/type/{id}")
    public String toEditType(@PathVariable Long id, Model model){
        Type type = typeService.getTypeById(id);
        if(type!=null)
            model.addAttribute("type",type); //保存修改前的记录
        else
            model.addAttribute("type",new Type()); //添加
        return "admin/types-input";
    }

    @PostMapping("/type/add")
    public String addType(Type type, RedirectAttributes attributes){
        Type t = typeService.getTypeByName(type.getName());
        if(t != null){
            attributes.addFlashAttribute("msg", "不能添加重复的分类");
            return "redirect:/admin/type/add";
        }else {
            typeService.addType(type);
            attributes.addFlashAttribute("msg", "添加成功");
        }
        return "redirect:/admin/types";   //不能直接跳转到types页面，否则不会显示type数据(没经过types方法)
    }

    @PostMapping("/type/edit")
    public String editType(Type type, RedirectAttributes attributes){  //修改
        Type t = typeService.getTypeByName(type.getName());
        if(t != null){
            attributes.addFlashAttribute("msg", "不能添加重复的分类");
            return "redirect:/admin/type/edit";
        }else {
            typeService.updateType(type);
            attributes.addFlashAttribute("msg", "修改成功");
        }
        return "redirect:/admin/types";   //不能直接跳转到types页面，否则不会显示type数据(没经过types方法)
    }

    //改变不了前端 还不会改后端 呵呵
    @GetMapping ("/type/delete/{id}")
    public String deleteType(@PathVariable Long id, RedirectAttributes attributes){
        if(typeService.deleteTypeById(id))
        {
            attributes.addFlashAttribute("msg", "删除成功");
            return "redirect:/admin/types";
        }
        return "error/404";
    }
}
