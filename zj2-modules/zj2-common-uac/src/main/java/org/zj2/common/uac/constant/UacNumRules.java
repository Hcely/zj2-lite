package org.zj2.common.uac.constant;

import org.zj2.common.sys.base.sequence.NumRule;

/**
 * UacNumRules
 *
 * @author peijie.ye
 * @date 2022/12/28 22:18
 */
public interface UacNumRules {
    NumRule EMPLOYEE_NO_RULE = new NumRule("UAC_ORG_EMPLOYEE_NO_RULE", "1{seq,######}", "{orgCode}");
}
