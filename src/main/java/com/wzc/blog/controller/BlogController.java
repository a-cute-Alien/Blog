package com.wzc.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzc.blog.pojo.Blog;
import com.wzc.blog.pojo.Tag;
import com.wzc.blog.pojo.Type;
import com.wzc.blog.pojo.vo.BlogInfo;
import com.wzc.blog.service.BlogService;
import com.wzc.blog.service.TagService;
import com.wzc.blog.service.TypeService;
import com.wzc.blog.util.IpUtils;
import com.wzc.blog.util.MarkdownUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "根据条件查找Blog")
@Controller
@RequestMapping("blog")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @ApiOperation("按id查找blog")
    @GetMapping("{id}")
    public String getBlogInfoById(@PathVariable("id") Long id, Model model, HttpServletRequest request) throws Exception {
        BlogInfo blogInfo = blogService.getBlogInfoById(id);
        if (blogInfo == null) {
            throw new NotFoundException("该博客不存在");
        }
        //将访问ip 缓存到 redis list
        redisTemplate.opsForSet().add(String.valueOf("BLOG_ID:"+id),IpUtils.getIpAddress(request));
        String content = blogInfo.getBlog().getContent();
        blogInfo.getBlog().setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        model.addAttribute("blogInfo", blogInfo);
        return "blog";
    }


    @ApiOperation("按关键字查找blog")
    @PostMapping("search")
    public String searchBlogs(@RequestParam String query, @RequestParam(required = false,defaultValue = "1",value = "pageNum")int pageNum, Model model){
        PageHelper.startPage(pageNum,4);
        List<BlogInfo> blogInfo = blogService.getBlogInfoByKeyWord(query);
        PageInfo pageInfo = new PageInfo(blogInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/type/{id}")
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

    @GetMapping("tag/{id}")
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

    @GetMapping("archive")
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


    @GetMapping("modle")
    @ResponseBody
    @ApiOperation("用于显示modle")
    public BlogInfo test(){
        BlogInfo blogInfo = new BlogInfo();
        return  blogInfo;
    }


}
