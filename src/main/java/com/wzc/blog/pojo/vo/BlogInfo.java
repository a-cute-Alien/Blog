package com.wzc.blog.pojo.vo;


import com.wzc.blog.pojo.*;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("Blog综合信息")
public class BlogInfo {
    private Long id;
    private Blog blog;
    private User user;
    private Type type;
    private String strTagId;
    private List<Tag> tagList = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
}
