package com.wzc.blog.service;

import com.wzc.blog.BlogApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest(classes = BlogApplication.class)
@RunWith(SpringRunner.class)
public class BlogServiceTest {

    @Autowired
    private BlogService blogService;
    @Test
    public void updateBlogView() {
        blogService.updateBlogView();
    }
}