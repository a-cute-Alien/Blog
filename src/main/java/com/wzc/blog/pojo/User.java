package com.wzc.blog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
@ApiModel("用户信息")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String email;
    private String avatar;
    private Integer type;
    @JsonIgnore
    private Date createTime;
    @JsonIgnore
    private Date updateTime;

}
