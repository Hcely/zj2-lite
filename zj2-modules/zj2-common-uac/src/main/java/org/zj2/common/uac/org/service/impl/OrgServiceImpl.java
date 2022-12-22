package org.zj2.common.uac.org.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.common.uac.org.dto.req.OrgCreateReq;
import org.zj2.common.uac.org.mapper.OrgMapper;
import org.zj2.common.uac.org.entity.Org;
import org.zj2.common.uac.org.service.OrgService;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.common.util.NumUtil;
import org.zj2.lite.service.BaseServiceImpl;
import org.zj2.lite.util.PatternUtil;

/**
 *  OrgServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class OrgServiceImpl extends BaseServiceImpl<OrgMapper, Org, OrgDTO> implements OrgService {
    @Override
    public OrgDTO getByCode(String orgCode) {
        return StringUtils.isEmpty(orgCode) ? null : getOne(wrapper().eq(OrgDTO::getOrgCode, orgCode));
    }

    @Override
    public OrgDTO create(OrgCreateReq req) {
        // 处理参数
        req.setOrgCode(StringUtils.trimToEmpty(req.getOrgCode()));
        req.setOrgName(StringUtils.trimToEmpty(req.getOrgName()));
        // 检验参数
        if (StringUtils.length(req.getOrgCode()) > 40) {throw ZRBuilder.failureErr("机构编码超过40个字");}
        if (!PatternUtil.isWord(req.getOrgCode())) {throw ZRBuilder.failureErr("机构编码格式不合法");}
        // 检查编码唯一性
        boolean exist = exists(wrapper().eq(OrgDTO::getOrgCode, req.getOrgCode()));
        if (exist) {throw ZRBuilder.failureErr("机构已存在");}
        // 插入机构
        OrgDTO org = new OrgDTO();
        org.setOrgCode(req.getOrgCode());
        org.setOrgName(req.getOrgName());
        org.setEnableFlag(1);
        org.setEnabledTime(DateUtil.now());
        return add(org);
    }


    @Override
    public void enable(String orgCode) {

    }

    @Override
    public void disable(String orgCode) {

    }
}
