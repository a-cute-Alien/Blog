package com.wzc.blog.pojo;


import com.wzc.blog.pojo.Type;
import com.wzc.blog.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogInfo {
    private Long id;
    private Blog blog;
    private User user;
    private Type type;
    private String strTagId;
    private List<Tag> tagList = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
}
