package org.zj2.common.uac.org.service.impl;

import org.springframework.stereotype.Service;
import org.zj2.common.uac.org.dto.OrgGroupDTO;
import org.zj2.common.uac.org.entity.OrgGroup;
import org.zj2.common.uac.org.mapper.OrgGroupMapper;
import org.zj2.common.uac.org.service.OrgGroupService;
import org.zj2.lite.service.BaseServiceImpl;

/**
 * OrgGroupServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class OrgGroupServiceImpl extends BaseServiceImpl<OrgGroupMapper, OrgGroup, OrgGroupDTO> implements OrgGroupService {
}
