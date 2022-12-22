package org.zj2.lite.common.entity.result;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.zj2.lite.common.util.StrUtil;

/**
 *  ZError
 *
 * @author peijie.ye
 * @date 2022/11/22 18:29
 */
@Getter
@Setter
@Accessors(chain = true)
public class ZError extends RuntimeException implements ZStatusMsg, Cloneable {
    private static final long serialVersionUID = -7187083936297078984L;
    private static final ZError INSTANCE = new ZError(true);

    public static ZError cloneInstance() {
        return INSTANCE.clone();
    }

    protected boolean success;//NOSONAR
    protected int status;//NOSONAR
    protected String msg;//NOSONAR

    private ZError(boolean ignored) {//NOSONAR
        this(false, SYS_ERROR_STATUS, null);
        setStackTrace(new StackTraceElement[]{new StackTraceElement("bizClass", "bizMethod", "bizClassFile", -1)});
    }

    public ZError() {
        this(false, SYS_ERROR_STATUS, null);
    }

    public ZError(Throwable cause) {
        super(cause);
        this.success = false;
        this.status = SYS_ERROR_STATUS;
    }

    public ZError(String msg) {
        this(false, SYS_ERROR_STATUS, msg);
    }

    public ZError(int status, String msg) {
        this(false, status, msg);
    }

    public ZError(boolean success, int status, String msg) {
        this.success = success;
        this.status = status;
        this.msg = msg;
    }

    public ZError setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ZError setMsg(String msg, Object arg0) {
        return setMsg(StrUtil.format(msg, arg0));
    }

    public ZError setMsg(String msg, Object arg0, Object arg1) {
        return setMsg(StrUtil.format(msg, arg0, arg1));
    }

    public ZError setMsg(String msg, Object arg0, Object arg1, Object arg2) {
        return setMsg(StrUtil.format(msg, arg0, arg1, arg2));
    }

    public ZError setMsg(String msg, Object arg0, Object arg1, Object arg2, Object arg3) {
        return setMsg(StrUtil.format(msg, arg0, arg1, arg2, arg3));
    }

    public ZError setMsg(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        return setMsg(StrUtil.format(msg, arg0, arg1, arg2, arg3, arg4));
    }

    public ZError setMsg(String msg, Object... args) {
        return setMsg(StrUtil.format(msg, args));
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public ZError clone() {//NOSONAR
        try {
            return (ZError) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new ZError();
        }
    }
}
