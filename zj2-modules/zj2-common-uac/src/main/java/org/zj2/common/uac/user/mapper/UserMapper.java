package org.zj2.common.uac.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.zj2.common.uac.user.entity.User;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
