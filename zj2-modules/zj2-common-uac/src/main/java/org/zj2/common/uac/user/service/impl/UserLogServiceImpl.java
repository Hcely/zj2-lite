package org.zj2.common.uac.user.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.zj2.common.uac.user.dto.UserLogDTO;
import org.zj2.common.uac.user.mapper.UserLogMapper;
import org.zj2.common.uac.user.entity.UserLog;
import org.zj2.common.uac.user.service.UserLogService;
import org.zj2.lite.service.context.ServiceRequestContext;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.service.BaseServiceImpl;
import org.zj2.lite.util.TransactionSyncUtil;

/**
 *  UserServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class UserLogServiceImpl extends BaseServiceImpl<UserLogMapper, UserLog, UserLogDTO> implements UserLogService {
    @Override
    public void addLogAfterCommit(UserLogDTO log) {
        // 初始化参数
        if (log.getLogTime() == null) {log.setLogTime(DateUtil.now());}
        log.setLogRemark(StringUtils.abbreviate(log.getLogRemark(), 250));
        log.setLogAddrIp(StringUtils.abbreviate(ServiceRequestContext.currentAttrIp(), 100));
        log.setLogDevice(StringUtils.abbreviate(ServiceRequestContext.currentDevice(), 250));
        // 事务提交
        TransactionSyncUtil.afterCommit(log, this::add);
    }
}
