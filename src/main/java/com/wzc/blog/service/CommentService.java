package com.wzc.blog.service;

import com.wzc.blog.mapper.CommentMapper;
import com.wzc.blog.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public List<Comment>getCommentByBlogId(Long blogId){
        Comment temp = new Comment();
        temp.setBlogId(blogId);
        return commentMapper.select(temp);
    }
}
