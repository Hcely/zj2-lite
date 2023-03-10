package org.zj2.lite.service.configure.mybatis;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import org.zj2.lite.service.entity.StrList;

/**
 * <br>CreateDate 九月 02,2022
 *
 * @author peijie.ye
 */
@Component
@MappedTypes(StrList.class)
public class MybatisStrListTypeHandler extends JacksonTypeHandler {
    public MybatisStrListTypeHandler() {
        super(StrList.class);
    }
}