package org.zj2.common.uac;

import org.zj2.lite.common.entity.ZReference;


/**
 * 命名空间: 1xxx
 */
public interface UacReferences {
    // 主表
    //
    ZReference USER = new ZReference(1001, "用户");
    //
    ZReference ORG = new ZReference(1011, "机构");
    ZReference ORG_GROUP = new ZReference(1012, "机构组");
    ZReference ORG_EMPLOYEE = new ZReference(1013, "员工");
    //
    ZReference ENTERPRISE = new ZReference(1021, "企业");
    //
    ZReference APP = new ZReference(1031, "应用");

    // 副表-------
    ZReference USER_VALUE = new ZReference(1501, "用户值");
    ZReference USER_PASSWORD = new ZReference(1502, "用户密码");
}
