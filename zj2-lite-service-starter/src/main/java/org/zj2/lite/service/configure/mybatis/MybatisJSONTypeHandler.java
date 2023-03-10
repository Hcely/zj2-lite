package org.zj2.lite.service.configure.mybatis;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

/**
 * <br>CreateDate 九月 02,2022
 *
 * @author peijie.ye
 */
@Component
@MappedTypes(JSONObject.class)
public class MybatisJSONTypeHandler extends JacksonTypeHandler {
    public MybatisJSONTypeHandler() {
        super(JSONObject.class);
    }
}