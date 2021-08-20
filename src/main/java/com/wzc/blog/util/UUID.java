package com.wzc.blog.util;

import tk.mybatis.mapper.genid.GenId;

public class UUID implements GenId<Long> {
    @Override
    public Long genId(String s, String s1) {
        return SnowflakeIdUtils.nextId();
    }
}
