package com.wzc.blog.mapper;

import com.wzc.blog.pojo.AccessRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;
import java.util.Set;

public interface IPMapper extends Mapper<AccessRecord>{

    @Insert({
            "<script>",
            "insert ignore into t_ip(id,ip) values",
            "<foreach collection='ipList' item='ip' separator=',' >",
            "(#{id},#{ip})",
            "</foreach>",
            "</script>"
    })
    int insertIgnore(@Param("id") Long id,@Param("ipList") Set<String> ipList);
}
