package org.zj2.lite.common.entity.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.common.util.StrUtil;

/**
 *  Resp
 *
 * @author peijie.ye
 * @date 2022/11/22 10:54
 */
@Getter
@Setter
@NoArgsConstructor
public class ZResult implements ZStatusMsg {
    private static final long serialVersionUID = 7639134027154889479L;
    protected boolean success;
    protected int status;
    protected String msg;


    public ZResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public ZResult setStatus(int status) {
        this.status = status;
        return this;
    }

    public ZResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ZResult setMsg(String msgFormat, Object arg0) {
        this.setMsg(StrUtil.format(msgFormat, arg0));
        return this;
    }

    public ZResult setMsg(String msgFormat, Object arg0, Object arg1) {
        return setMsg(StrUtil.format(msgFormat, arg0, arg1));
    }

    public ZResult setMsg(String msgFormat, Object arg0, Object arg1, Object arg2) {
        return setMsg(StrUtil.format(msgFormat, arg0, arg1, arg2));
    }

    public ZResult setMsg(String msgFormat, Object arg0, Object arg1, Object arg2, Object arg3) {
        return setMsg(StrUtil.format(msgFormat, arg0, arg1, arg2, arg3));
    }

    public ZResult setMsg(String msgFormat, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        return setMsg(StrUtil.format(msgFormat, arg0, arg1, arg2, arg3, arg4));
    }

    public ZResult setMsg(String msgFormat, Object... args) {
        return setMsg(StrUtil.format(msgFormat, args));
    }
}
