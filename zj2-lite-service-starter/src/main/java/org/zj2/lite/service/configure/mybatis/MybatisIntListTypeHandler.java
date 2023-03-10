package org.zj2.lite.service.configure.mybatis;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import org.zj2.lite.service.entity.IntList;

/**
 * <br>CreateDate 九月 02,2022
 *
 * @author peijie.ye
 */
@Component
@MappedTypes(IntList.class)
public class MybatisIntListTypeHandler extends JacksonTypeHandler {
    public MybatisIntListTypeHandler() {
        super(IntList.class);
    }
}