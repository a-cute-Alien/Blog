package com.wzc.blog.mapper;

import com.wzc.blog.pojo.BlogInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BlogInfoMapper {
    public List<BlogInfo> getAllBlogInfo();
    public BlogInfo getBlogInfoById(@Param("id")Long id);
    public List<BlogInfo> getBlogInfoByTypeId(@Param("typeId")Long typeId);
    public List<BlogInfo> getBlogInfoByTagId(@Param("tagId")Long tagId);
    public List<BlogInfo> getBlogInfoByKeyWord(@Param("query")String query);
    public List<BlogInfo> getBlogInfoBySelective(BlogInfo blogInfo);
}
