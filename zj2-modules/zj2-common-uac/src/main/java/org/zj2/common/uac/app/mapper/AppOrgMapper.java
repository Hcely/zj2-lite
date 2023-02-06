package org.zj2.common.uac.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zj2.common.uac.app.dto.AppOrgExtDTO;
import org.zj2.common.uac.app.dto.req.AppOrgQuery;
import org.zj2.common.uac.app.entity.AppOrg;

import java.util.List;

/**
 * <p>
 * 应用机构关系表 Mapper 接口
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Mapper
public interface AppOrgMapper extends BaseMapper<AppOrg> {
    List<AppOrgExtDTO> query(@Param("query") AppOrgQuery query);
}
