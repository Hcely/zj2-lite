package org.zj2.lite.service.configure.mybatis;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>CreateDate 九月 02,2022
 *
 * @author peijie.ye
 */
@Component
@MappedTypes(List.class)
public class MybatisListTypeHandler extends JacksonTypeHandler {
    public MybatisListTypeHandler() {
        super(ArrayList.class);
    }
}