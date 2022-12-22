package org.zj2.lite.service.configure.mybatis;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

/**
 *
 * <br>CreateDate 九月 02,2022
 * @author peijie.ye
 */
@Component
@MappedTypes(JSONArray.class)
public class MybatisJSONArrayTypeHandler extends JacksonTypeHandler {
    public MybatisJSONArrayTypeHandler() {
        super(JSONArray.class);
    }
}