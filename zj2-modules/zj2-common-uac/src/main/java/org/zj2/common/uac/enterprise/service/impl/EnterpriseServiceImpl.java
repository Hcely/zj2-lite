package org.zj2.common.uac.enterprise.service.impl;

import org.springframework.stereotype.Service;
import org.zj2.common.uac.enterprise.dto.EnterpriseDTO;
import org.zj2.common.uac.enterprise.entity.Enterprise;
import org.zj2.common.uac.enterprise.mapper.EnterpriseMapper;
import org.zj2.common.uac.enterprise.service.EnterpriseService;
import org.zj2.lite.service.BaseServiceImpl;

/**
 * EnterpriseServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class EnterpriseServiceImpl extends BaseServiceImpl<EnterpriseMapper, Enterprise, EnterpriseDTO> implements EnterpriseService {
}
