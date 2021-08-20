package com.wzc.blog.service;

import com.wzc.blog.mapper.BlogInfoMapper;
import com.wzc.blog.mapper.BlogMapper;
import com.wzc.blog.mapper.TagMapMapper;
import com.wzc.blog.pojo.Blog;
import com.wzc.blog.pojo.TagMap;
import com.wzc.blog.pojo.vo.BlogInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BlogService {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private TagMapMapper tagMapMapper;
    @Autowired
    private BlogInfoMapper blogInfoMapper;
    @Autowired
    private IPService ipService;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;



    public Blog getBlogById(Long id) {
        return blogMapper.selectByPrimaryKey(id);
    }

    public List<Blog> getAllBlogs() {
        return blogMapper.selectAll();
    }

    public List<Blog> getBlogsByTypeId(Long typeId){
        Blog template = new Blog();
        template.setTypeId(typeId);
        return blogMapper.select(template);
    }

    public List<Blog> getBlogsByTagId(Long tagId){
        return blogMapper.selectBlogByTagId(tagId);
    }


    public List<Blog> getBlogsByTitle(String query){
        Example example = new Example(Blog.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtil.isEmpty(query)){
            criteria.andLike("title", "%" + query + "%");
        }
        return blogMapper.selectByExample(example);
    }
    public Map<String, List<Blog>> getBlogsByUpdateTime(){
        List<String> years = blogMapper.selectUpdateYear();
        Set<String> set = new HashSet<>(years);  //set去掉重复的年份
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : set) {
            map.put(year, blogMapper.selectBlogByUpdateYear(year));
        }
        return map;
    }
    public List<Blog> getBlogsByRecommend(){
        return blogMapper.selectBlogByRecommend();
    }

    public List<BlogInfo> getAllBlogInfo() {
        return blogInfoMapper.getAllBlogInfo();
    }
    public List<BlogInfo> getBlogInfoByTypeId(Long typeId){
        return blogInfoMapper.getBlogInfoByTypeId(typeId);
    }
    public List<BlogInfo> getBlogInfoByTagId(Long tagId){
        return blogInfoMapper.getBlogInfoByTagId(tagId);
    }
    public List<BlogInfo> getBlogInfoByKeyWord(String query){
        return blogInfoMapper.getBlogInfoByKeyWord(query);
    }
    public List<BlogInfo> getBlogInfoBySelective(BlogInfo blogInfo){
        return blogInfoMapper.getBlogInfoBySelective(blogInfo);
    }
    public BlogInfo getBlogInfoById(Long id) {
        return blogInfoMapper.getBlogInfoById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addBlog(BlogInfo blogInfo){
        blogInfo.getBlog().setCreateTime(new Date());
        blogInfo.getBlog().setUpdateTime(new Date());
        blogInfo.getBlog().setViews(0);
        blogInfo.getBlog().setPublished(true);
        if(blogMapper.insertSelective(blogInfo.getBlog())>0){
            long[] tagIds=Arrays.stream(blogInfo.getStrTagId().split(",")).mapToLong(Long::valueOf).toArray();
            for(long tagId : tagIds){
                TagMap tagMap = new TagMap();
                tagMap.setBlogId(blogInfo.getBlog().getId());
                tagMap.setTagId(tagId);
                tagMapMapper.insertSelective(tagMap);
            }
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean editBlog(BlogInfo blogInfo){
        TagMap t = new TagMap();
        t.setBlogId(blogInfo.getId());
        tagMapMapper.delete(t);
        long[] tagIds=Arrays.stream(blogInfo.getStrTagId().split(",")).mapToLong(Long::valueOf).toArray();
        for(long tagId : tagIds){
            TagMap tagMap = new TagMap();
            tagMap.setBlogId(blogInfo.getId());
            tagMap.setTagId(tagId);
            tagMapMapper.insert(tagMap);
        }
        blogInfo.getBlog().setUpdateTime(new Date());
        return blogMapper.updateByPrimaryKeySelective(blogInfo.getBlog())>0;
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBlogById(Long id){
        Blog blog = getBlogById(id);
        TagMap template = new TagMap();
        template.setBlogId(blog.getId());
        return tagMapMapper.delete(template) >= 0 && blogMapper.deleteByPrimaryKey(blog.getId()) > 0;
    }

    @Scheduled(cron = "* * */2 * * ?")
    @Transactional()
    public boolean updateBlogView(){
        //查询所有信息 sql效率低
//        List<Long> idList = blogMapper.selectAll()
//                .stream()
//                .mapToLong((blog)->{return blog.getId();})
//                .boxed()
//                .collect(Collectors.toList());

        //索引覆盖 不需要回表
        List<Long> idList = blogMapper.selectAllBlogId();
        for(long id:idList){
            String key = "BLOG_ID:"+id;
            Set<Object> ipList = redisTemplate.opsForSet().members(key);
            if(!ipList.isEmpty()){
                Set<String> ips=ipList.stream().map((o)->{return (String)o;}).collect(Collectors.toSet());
                //更新ip表
                ipService.addIPList(id,ips);
                //更新view
                int newView = ipService.getBlogView(id);
                Blog temp = new Blog();
                temp.setId(id);
                temp.setViews(newView);
                blogMapper.updateByPrimaryKeySelective(temp);
            }
        }
        return true;
    }
}
