package org.zj2.common.uac.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zj2.common.uac.app.entity.App;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 应用表 Mapper 接口
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Mapper
public interface AppMapper extends BaseMapper<App> {

}
