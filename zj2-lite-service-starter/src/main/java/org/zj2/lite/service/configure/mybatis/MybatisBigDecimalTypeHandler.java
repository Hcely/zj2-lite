package org.zj2.lite.service.configure.mybatis;

import org.apache.ibatis.type.BigDecimalTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.constant.NoneConstants;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <br>CreateDate 九月 02,2022
 *
 * @author peijie.ye
 */
@Component
@MappedTypes(BigDecimal.class)
public class MybatisBigDecimalTypeHandler extends BigDecimalTypeHandler {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BigDecimal parameter, JdbcType jdbcType) throws SQLException {
        parameter = parameter == NoneConstants.NONE_NUM ? null : parameter;
        super.setNonNullParameter(ps, i, parameter, jdbcType);
    }
}