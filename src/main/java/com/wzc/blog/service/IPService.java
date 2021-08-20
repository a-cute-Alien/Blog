package com.wzc.blog.service;

import com.wzc.blog.mapper.IPMapper;
import com.wzc.blog.pojo.AccessRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class IPService {
    @Autowired
    private IPMapper ipMapper;
    public boolean addIPList(Long id, Set<String> ips){
        return ipMapper.insertIgnore(id,ips)>0;
    }

    public int getBlogView(Long id){
        AccessRecord temp = new AccessRecord();
        temp.setId(id);
        return  ipMapper.selectCount(temp);
    }
}
