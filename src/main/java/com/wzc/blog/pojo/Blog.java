package com.wzc.blog.pojo;

import com.wzc.blog.util.UUID;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_blog")
@ApiModel("博客基本信息")
public class Blog {

    @Id
    @KeySql(genId = UUID.class)
    private Long id;
    private String title;
    private String content;
    private String firstPicture;
    private String flag;
    private Integer views;
    private Boolean appreciation;
    private Boolean shareStatement;
    private Boolean commentabled;
    private Boolean published;
    private Boolean recommend;
    private Date createTime;
    private Date updateTime;
    private Long typeId;
    private Long userId;
    private String description;

}
