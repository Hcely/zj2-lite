package org.zj2.common.uac.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.zj2.common.uac.user.entity.UserPassword;

/**
 * <p>
 * 用户密码表 Mapper 接口
 * </p>
 *
 * @author peijie.ye
 * @since 2022-12-01
 */
@Mapper
public interface UserPasswordMapper extends BaseMapper<UserPassword> {

}
