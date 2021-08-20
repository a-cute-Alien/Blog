package com.wzc.blog.service;

import com.wzc.blog.BlogApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;


@SpringBootTest(classes = BlogApplication.class)
@RunWith(SpringRunner.class)
public class IPServiceTest {

    @Autowired
    private IPService ipService;

    @Test
    public void addIPListTest(){
        Long id = 13L;
        Set<String> ips = new HashSet<>();
        ips.add("1.2.3.4");
        ips.add("4,5,6,7");
        ipService.addIPList(id,ips);
    }

    @Test
    public void getBlogView() {
        System.out.println(ipService.getBlogView(13L));
    }
}