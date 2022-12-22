package org.zj2.lite.common.entity.result;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *  Resp
 *
 * @author peijie.ye
 * @date 2022/11/22 10:54
 */
@Getter
@Setter
@Accessors(chain = true)
public class ZResp<T> extends ZResult {
    private static final long serialVersionUID = 7639134027154889479L;
    protected T data;//NOSONAR


    @Override
    public ZResp<T> setStatus(int status) {
        super.setStatus(status);
        return this;
    }

    @Override
    public ZResp<T> setSuccess(boolean success) {
        super.setSuccess(success);
        return this;
    }

    @Override
    public ZResp<T> setMsg(String msg) {
        super.setMsg(msg);
        return this;
    }

    @Override
    public ZResp<T> setMsg(String msgFormat, Object arg0) {
        super.setMsg(msgFormat, arg0);
        return this;
    }

    @Override
    public ZResp<T> setMsg(String msgFormat, Object arg0, Object arg1) {
        super.setMsg(msgFormat, arg0, arg1);
        return this;
    }

    @Override
    public ZResp<T> setMsg(String msgFormat, Object arg0, Object arg1, Object arg2) {
        super.setMsg(msgFormat, arg0, arg1, arg2);
        return this;
    }

    @Override
    public ZResp<T> setMsg(String msgFormat, Object arg0, Object arg1, Object arg2, Object arg3) {
        super.setMsg(msgFormat, arg0, arg1, arg2, arg3);
        return this;
    }

    @Override
    public ZResp<T> setMsg(String msgFormat, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        super.setMsg(msgFormat, arg0, arg1, arg2, arg3, arg4);
        return this;
    }

    @Override
    public ZResp<T> setMsg(String msgFormat, Object... args) {
        super.setMsg(msgFormat, args);
        return this;
    }
}
