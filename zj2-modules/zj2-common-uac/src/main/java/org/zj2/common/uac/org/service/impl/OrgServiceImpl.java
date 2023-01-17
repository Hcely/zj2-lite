package org.zj2.common.uac.org.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.common.uac.org.dto.req.OrgEditReq;
import org.zj2.common.uac.org.dto.req.OrgQuery;
import org.zj2.common.uac.org.entity.Org;
import org.zj2.common.uac.org.mapper.OrgMapper;
import org.zj2.common.uac.org.service.OrgService;
import org.zj2.lite.common.constant.NoneConstants;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.common.util.PatternUtil;
import org.zj2.lite.service.BaseServiceImpl;
import org.zj2.lite.service.context.AuthenticationContext;

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
    public OrgDTO create(OrgEditReq req) {
        // 处理参数
        req.setOrgCode(StringUtils.trimToEmpty(req.getOrgCode()));
        req.setOrgName(StringUtils.trimToEmpty(req.getOrgName()));
        // 检验参数
        if (StringUtils.length(req.getOrgCode()) > 60) { throw ZRBuilder.failureErr("机构编码超过60个字"); }
        if (!PatternUtil.isWord(req.getOrgCode())) { throw ZRBuilder.failureErr("机构编码格式不合法"); }
        // 检查编码唯一性
        boolean exist = exists(wrapper().eq(OrgDTO::getOrgCode, req.getOrgCode()));
        if (exist) { throw ZRBuilder.failureErr("机构已存在"); }
        // 插入机构
        AuthenticationContext.current().setOrgCode(req.getOrgCode());
        OrgDTO org = new OrgDTO();
        org.setOrgCode(req.getOrgCode());
        org.setOrgName(req.getOrgName());
        org.setEnableFlag(1);
        org.setEnabledTime(DateUtil.now());
        return add(org);
    }

    @Override
    public void edit(OrgEditReq req) {
        // 处理参数
        OrgDTO org = getByCode(req.getOrgCode());
        if (org != null) {
            OrgDTO update = new OrgDTO();
            update.setOrgId(org.getOrgId());
            update.setOrgName(req.getOrgName());
            updateById(update);
        }
    }


    @Override
    public void enable(String orgCode) {
        OrgDTO org = getByCode(orgCode);
        if (org != null && BooleanUtil.isFalse(org.getEnableFlag())) {
            OrgDTO update = new OrgDTO();
            update.setOrgId(org.getOrgId());
            update.setEnableFlag(1);
            update.setEnabledTime(DateUtil.now());
            update.setDisabledTime(NoneConstants.NONE_DATE);
            updateById(update);
        }
    }

    @Override
    public void disable(String orgCode) {
        OrgDTO org = getByCode(orgCode);
        if (org != null && BooleanUtil.isTrue(org.getEnableFlag())) {
            OrgDTO update = new OrgDTO();
            update.setOrgId(org.getOrgId());
            update.setEnableFlag(0);
            update.setDisabledTime(DateUtil.now());
            updateById(update);
        }
    }

    @Override
    public ZListResp<OrgDTO> pageQuery(OrgQuery query) {
        return null;
    }
}
