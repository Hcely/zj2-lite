package org.zj2.common.uac.org.service.impl;

import org.springframework.stereotype.Service;
import org.zj2.common.uac.org.dto.OrgEmployeeLogDTO;
import org.zj2.common.uac.org.entity.OrgEmployeeLog;
import org.zj2.common.uac.org.mapper.OrgEmployeeLogMapper;
import org.zj2.common.uac.org.service.OrgEmployeeLogService;
import org.zj2.lite.service.BaseServiceImpl;

/**
 * OrgEmployeeLogServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class OrgEmployeeLogServiceImpl extends BaseServiceImpl<OrgEmployeeLogMapper, OrgEmployeeLog, OrgEmployeeLogDTO>
        implements OrgEmployeeLogService {
}
