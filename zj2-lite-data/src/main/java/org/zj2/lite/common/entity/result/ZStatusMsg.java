package org.zj2.lite.common.entity.result;

import java.io.Serializable;

/**
 *  IResp
 *
 * @author peijie.ye
 * @date 2022/11/22 10:58
 */
public interface ZStatusMsg extends Serializable {
    int SUCCESS_STATUS = 200;
    int FAILURE_STATUS = 400;
    int SYS_ERROR_STATUS = 500;

    boolean isSuccess();

    int getStatus();

    String getMsg();
}
