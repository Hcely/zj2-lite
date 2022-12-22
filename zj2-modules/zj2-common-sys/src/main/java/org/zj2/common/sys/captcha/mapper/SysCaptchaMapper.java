package org.zj2.common.sys.captcha.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zj2.common.sys.captcha.entity.SysCaptcha;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 验证码表 Mapper 接口
 * </p>
 *
 * @author peijie.ye
 * @since 2022-12-10
 */
@Mapper
public interface SysCaptchaMapper extends BaseMapper<SysCaptcha> {

}
