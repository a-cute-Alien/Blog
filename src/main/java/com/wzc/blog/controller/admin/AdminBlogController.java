package com.wzc.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzc.blog.pojo.Blog;
import com.wzc.blog.pojo.vo.BlogInfo;
import com.wzc.blog.pojo.Tag;
import com.wzc.blog.service.BlogService;
import com.wzc.blog.service.TagService;
import com.wzc.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminBlogController {

    @Autowired
    private TagService tagService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    public void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.getAllType());
        model.addAttribute("tags", tagService.getAllTag());
    }
    @GetMapping("blogs")  //后台显示博客列表
    public String blogs(@RequestParam(required = false,defaultValue = "1",value = "pageNum")int pagenum, Model model){

        PageHelper.startPage(pagenum, 8);
        List<BlogInfo> allBlog = blogService.getAllBlogInfo();
        //得到分页结果对象
        PageInfo pageInfo = new PageInfo(allBlog);
        model.addAttribute("pageInfo", pageInfo);
        setTypeAndTag(model);  //查询类型和标签
        return "admin/blogs";
    }

    @PostMapping("blogs/search")  //按条件查询博客
    public String searchBlogs(BlogInfo blogInfo, @RequestParam(required = false,defaultValue = "1",value = "pageNum")int pagenum, Model model){
        PageHelper.startPage(pagenum, 8);
        List<BlogInfo> allBlog = blogService.getBlogInfoBySelective(blogInfo);
        //得到分页结果对象
        PageInfo pageInfo = new PageInfo(allBlog);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("message", "查询成功");
        setTypeAndTag(model);
        return "admin/blogs";
    }

    @GetMapping("blog/{id}") //去编辑博客页面
    public String toEditBlog(@PathVariable Long id, Model model){
        BlogInfo blogInfo = blogService.getBlogInfoById(id);
        if(blogInfo!=null)
        {
            StringBuffer buffer = new StringBuffer();
            for(Tag tag:blogInfo.getTagList()){
                buffer.append(tag.getId()+",");
            }
            buffer.deleteCharAt(buffer.length()-1);
            blogInfo.setStrTagId(buffer.toString());
            model.addAttribute("blogInfo", blogInfo); //返回一个blog对象给前端th:object
        }
        else
        {
            BlogInfo temp = new BlogInfo();
            temp.setBlog(new Blog());
            model.addAttribute("blogInfo",temp);
        }
        setTypeAndTag(model);
        return "admin/blogs-input";
    }

    @PostMapping("blog/add") //新增、编辑博客
    public String addBlog(BlogInfo blogInfo, HttpSession session, RedirectAttributes attributes){
        blogService.addBlog(blogInfo);
        return "redirect:/admin/blogs";
    }

    @PostMapping("blog/edit") //新增、编辑博客
    public String editBlog(BlogInfo blogInfo, HttpSession session, RedirectAttributes attributes){
        blogService.editBlog(blogInfo);
        return "redirect:/admin/blogs";
    }


    @GetMapping("blog/delete/{id}")
    public String deleteBlogs(@PathVariable Long id, RedirectAttributes attributes){
        if(blogService.deleteBlogById(id))
            attributes.addFlashAttribute("msg", "删除成功");
        return "redirect:/admin/blogs";
    }
}
