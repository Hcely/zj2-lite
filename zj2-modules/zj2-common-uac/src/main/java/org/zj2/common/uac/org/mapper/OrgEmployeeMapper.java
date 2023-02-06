package org.zj2.common.uac.org.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zj2.common.uac.org.dto.OrgEmployeeExtDTO;
import org.zj2.common.uac.org.dto.req.OrgEmployeeQuery;
import org.zj2.common.uac.org.entity.OrgEmployee;

import java.util.List;

/**
 * <p>
 * 员工表 Mapper 接口
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Mapper
public interface OrgEmployeeMapper extends BaseMapper<OrgEmployee> {
    List<OrgEmployeeExtDTO> query(@Param("query") OrgEmployeeQuery query);
}
