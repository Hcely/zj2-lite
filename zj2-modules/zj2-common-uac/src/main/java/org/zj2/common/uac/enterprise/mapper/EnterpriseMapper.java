package org.zj2.common.uac.enterprise.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zj2.common.uac.enterprise.entity.Enterprise;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 企业表 Mapper 接口
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Mapper
public interface EnterpriseMapper extends BaseMapper<Enterprise> {

}
