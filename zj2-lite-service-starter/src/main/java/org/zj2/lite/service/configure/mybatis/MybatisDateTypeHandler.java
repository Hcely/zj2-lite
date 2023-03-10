package org.zj2.lite.service.configure.mybatis;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.util.DateUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * <br>CreateDate 九月 02,2022
 *
 * @author peijie.ye
 */
@Component
@MappedTypes(LocalDateTime.class)
public class MybatisDateTypeHandler extends LocalDateTimeTypeHandler {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        parameter = DateUtil.isInvalid(parameter) ? null : parameter;
        super.setNonNullParameter(ps, i, parameter, jdbcType);
    }
}
