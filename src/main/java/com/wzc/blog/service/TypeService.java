package com.wzc.blog.service;

import com.wzc.blog.mapper.TypeMapper;
import com.wzc.blog.pojo.Blog;
import com.wzc.blog.pojo.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeService {

    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private BlogService blogService;

    public Type getTypeById(Long id){
        return typeMapper.selectByPrimaryKey(id);
    }
    public Type getTypeByName(String name) {
        Type temp = new Type();
        temp.setName(name);
        return  typeMapper.selectOne(temp);
    }
    public List<Type> getAllType(){
        return typeMapper.selectAll();
    }


    public  boolean addType(Type type){
        return  typeMapper.insert(type)>0;
    }

    public boolean updateType(Type type){
        return  typeMapper.updateByPrimaryKey(type)>0;
    }

    @Transactional
    public boolean deleteTypeById(Long id){
        List<Blog> blogList = blogService.getBlogsByTypeId(id);
        for(Blog blog:blogList){
            blogService.deleteBlogById(id);
        }
        return typeMapper.deleteByPrimaryKey(id)>0;
    }



}
