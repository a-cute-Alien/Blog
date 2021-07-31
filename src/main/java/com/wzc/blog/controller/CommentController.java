package com.wzc.blog.controller;

import com.wzc.blog.service.BlogService;
import com.wzc.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("{blogId}")  //展示留言
    public String comments(@PathVariable("blogId") Long blogId, Model model){
        model.addAttribute("commentList", commentService.getCommentByBlogId(blogId));
//        model.addAttribute("blog", blogService.getDetailedBlog(blogId));
        return "blog :: commentList";
    }
}
