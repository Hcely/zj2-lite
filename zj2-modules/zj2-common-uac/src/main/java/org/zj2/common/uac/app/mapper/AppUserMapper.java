package org.zj2.common.uac.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.zj2.common.uac.app.entity.AppOrg;
import org.zj2.common.uac.app.entity.AppUser;

/**
 * <p>
 * 应用机构关系表 Mapper 接口
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {

}
