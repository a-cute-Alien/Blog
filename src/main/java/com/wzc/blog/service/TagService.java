package com.wzc.blog.service;

import com.wzc.blog.mapper.TagMapper;
import com.wzc.blog.pojo.Tag;
import com.wzc.blog.pojo.TagMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagMapper tagMapper;

    public Tag getTagById(Long id){
        return  tagMapper.selectByPrimaryKey(id);
    }

    public List<Tag> getAllTag(){
        return  tagMapper.selectAll();
    }

    public Tag getTagByName(String name){
        Tag temp = new Tag();
        temp.setName(name);
        return tagMapper.selectOne(temp);
    }
    public boolean addTag(Tag tag){
        return  tagMapper.insert(tag)>0;
    }
    public boolean updateTag(Tag tag){
        return  tagMapper.updateByPrimaryKey(tag)>0;
    }
    public boolean deleteTag(Long id){
        return  tagMapper.deleteByPrimaryKey(id)>0;
    }
}
