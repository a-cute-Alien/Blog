package com.wzc.blog.mapper;

import com.wzc.blog.pojo.Blog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BlogMapper extends Mapper<Blog> {

    @Select("SELECT * FROM t_blog WHERE id in( SELECT blog_id FROM t_blog_tags WHERE t_blog_tags.tag_id = #{tag_id})")
    List<Blog> selectBlogByTagId(@Param("tag_id")Long tagId);

    @Select("select * from t_blog where recommend = 1 order by update_time desc limit 1,5 ")
    List<Blog> selectBlogByRecommend();

    @Select("select DATE_FORMAT(b.update_time, '%Y') from t_blog b order by b.update_time desc")
    List<String> selectUpdateYear();

    @Select("select b.title, b.update_time, b.id, b.flag from t_blog b where DATE_FORMAT(b.update_time, \"%Y\") = #{year}")
    List<Blog> selectBlogByUpdateYear(@Param("year")String year);

    @Select("select id from t_blog")
    List<Long> selectAllBlogId();
}
