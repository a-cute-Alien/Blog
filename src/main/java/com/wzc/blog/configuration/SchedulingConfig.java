package com.wzc.blog.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@Slf4j
public class SchedulingConfig {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

//    @Scheduled(cron = "*/15 * * * * ?")
//    public void updateView(){
//        String key = "BLOG_ID:13";
//        Set<Object> ipList = redisTemplate.opsForSet().members(key);
//        Set<String> ips=ipList.stream().map((o)->{return (String)o;}).collect(Collectors.toSet());
//        for(String ip:ips){
//            log.info( ip );
//        }
//    }
}
