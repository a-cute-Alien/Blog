package com.wzc.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_ip")
public class AccessRecord {


    @Id
    private Long id;
    @Id
    private String ip;

}
