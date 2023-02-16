package org.zj2.lite.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.zj2.lite.common.entity.result.ZError;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.entity.result.ZResp;
import org.zj2.lite.common.entity.result.ZResult;
import org.zj2.lite.common.entity.result.ZStatusMsg;
import org.zj2.lite.common.util.StrUtil;
import org.zj2.lite.message.MessageUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ZResultBuilder
 *
 * @author peijie.ye
 * @date 2022/11/23 15:14
 */
public class ZRBuilder implements Serializable {
    private static final long serialVersionUID = -2497487035627586056L;
    public static final String NAMESPACE = "message";


    public static ZRBuilder builder() {
        return new ZRBuilder();
    }

    public static ZRBuilder builder(String msg) {
        return new ZRBuilder().msg(msg);
    }

    public static ZRBuilder success() {
        return new ZRBuilder().success(true).status(ZStatusMsg.SUCCESS_STATUS);
    }

    public static ZRBuilder failure() {
        return new ZRBuilder().success(false).status(ZStatusMsg.FAILURE_STATUS);
    }

    public static ZResult successResult() {
        return new ZResult().setSuccess(true).setStatus(ZStatusMsg.SUCCESS_STATUS);
    }

    public static <T> ZResp<T> successResp(T data) {
        return new ZResp<>(data);
    }

    public static ZError failureErr(String msg) {
        return ZError.cloneInstance().setSuccess(false).setStatus(ZStatusMsg.FAILURE_STATUS).setMsg(msg);
    }

    public static ZError failureErr(String msgFormat, Object arg0) {
        return ZError.cloneInstance().setSuccess(false).setStatus(ZStatusMsg.FAILURE_STATUS).setMsg(msgFormat, arg0);
    }

    public static ZError failureErr(String msgFormat, Object arg0, Object arg1) {
        return ZError.cloneInstance().setSuccess(false).setStatus(ZStatusMsg.FAILURE_STATUS)
                .setMsg(msgFormat, arg0, arg1);
    }

    public static ZError failureErr(String msgFormat, Object arg0, Object arg1, Object arg2) {
        return ZError.cloneInstance().setSuccess(false).setStatus(ZStatusMsg.FAILURE_STATUS)
                .setMsg(msgFormat, arg0, arg1, arg2);
    }

    public static ZError failureErr(String msgFormat, Object arg0, Object arg1, Object arg2, Object arg3) {
        return ZError.cloneInstance().setSuccess(false).setStatus(ZStatusMsg.FAILURE_STATUS)
                .setMsg(msgFormat, arg0, arg1, arg2, arg3);
    }

    public static ZError failureErr(String msgFormat, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        return ZError.cloneInstance().setSuccess(false).setStatus(ZStatusMsg.FAILURE_STATUS)
                .setMsg(msgFormat, arg0, arg1, arg2, arg3, arg4);
    }

    public static ZError failureErr(String msgFormat, Object... args) {
        return ZError.cloneInstance().setSuccess(false).setStatus(ZStatusMsg.FAILURE_STATUS).setMsg(msgFormat, args);
    }


    public static ZRBuilder builder(boolean success) {
        return new ZRBuilder().success(success);
    }

    protected Boolean success;
    protected int status;
    protected String module;
    protected String msg;
    protected String errorSeparator = ",";
    protected List<String> errors;//NOSONAR

    public ZRBuilder success(boolean success) {
        this.success = success;
        return this;
    }

    public ZRBuilder status(int status) {
        this.status = status;
        return this;
    }

    public ZRBuilder module(String module) {
        this.module = of(module);
        return this;
    }

    public ZRBuilder msg(String msg) {
        msg = of(msg);
        if (StringUtils.isNotEmpty(module) && StringUtils.isNotEmpty(msg)) {
            msg0(StrUtil.concat(module, '-', msg));
        } else {
            msg0(msg);
        }
        return this;
    }

    private ZRBuilder msg0(String msg) {
        this.msg = msg;
        return this;
    }

    public ZRBuilder msg(String msgFormat, Object arg0) {
        msgFormat = of(msgFormat);
        if (StringUtils.isEmpty(module)) {
            return msg(StrUtil.format(msgFormat, arg0));
        } else {
            int initialCapacity = module.length() + StringUtils.length(msgFormat) + 32 * 2;
            TextStringBuilder sb = new TextStringBuilder(initialCapacity).append(module).append('-');
            StrUtil.getFormatter(msgFormat).appendArgs(sb, arg0);
            return msg(sb.toString());
        }
    }

    public ZRBuilder msg(String msgFormat, Object arg0, Object arg1) {
        msgFormat = of(msgFormat);
        if (StringUtils.isEmpty(module)) {
            return msg(StrUtil.format(msgFormat, arg0, arg1));
        } else {
            int initialCapacity = module.length() + StringUtils.length(msgFormat) + 32 * 2;
            TextStringBuilder sb = new TextStringBuilder(initialCapacity).append(module).append('-');
            StrUtil.getFormatter(msgFormat).appendArgs(sb, arg0, arg1);
            return msg(sb.toString());
        }
    }

    public ZRBuilder msg(String msgFormat, Object arg0, Object arg1, Object arg2) {
        msgFormat = of(msgFormat);
        if (StringUtils.isEmpty(module)) {
            return msg(StrUtil.format(msgFormat, arg0, arg1, arg2));
        } else {
            int initialCapacity = module.length() + StringUtils.length(msgFormat) + 32 * 3;
            TextStringBuilder sb = new TextStringBuilder(initialCapacity).append(module).append('-');
            StrUtil.getFormatter(msgFormat).appendArgs(sb, arg0, arg1, arg2);
            return msg(sb.toString());
        }
    }

    public ZRBuilder msg(String msgFormat, Object arg0, Object arg1, Object arg2, Object arg3) {
        msgFormat = of(msgFormat);
        if (StringUtils.isEmpty(module)) {
            return msg(StrUtil.format(msgFormat, arg0, arg1, arg2, arg3));
        } else {
            int initialCapacity = module.length() + StringUtils.length(msgFormat) + 32 * 4;
            TextStringBuilder sb = new TextStringBuilder(initialCapacity).append(module).append('-');
            StrUtil.getFormatter(msgFormat).appendArgs(sb, arg0, arg1, arg2, arg3);
            return msg(sb.toString());
        }
    }

    public ZRBuilder msg(String msgFormat, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        msgFormat = of(msgFormat);
        if (StringUtils.isEmpty(module)) {
            return msg(StrUtil.format(msgFormat, arg0, arg1, arg2, arg3, arg4));
        } else {
            int initialCapacity = module.length() + StringUtils.length(msgFormat) + 32 * 4;
            TextStringBuilder sb = new TextStringBuilder(initialCapacity).append(module).append('-');
            StrUtil.getFormatter(msgFormat).appendArgs(sb, arg0, arg1, arg2, arg3, arg4);
            return msg(sb.toString());
        }
    }

    public ZRBuilder msg(String msgFormat, Object... args) {
        msgFormat = of(msgFormat);
        if (StringUtils.isEmpty(module)) {
            return msg(StrUtil.format(msgFormat, args));
        } else {
            int initialCapacity = module.length() + StringUtils.length(msgFormat) + 32 * 4;
            TextStringBuilder sb = new TextStringBuilder(initialCapacity).append(module).append('-');
            StrUtil.getFormatter(msgFormat).appendArgs(sb, args);
            return msg(sb.toString());
        }
    }

    public ZRBuilder addError(String msg) {
        return addError0(of(msg));
    }

    public ZRBuilder addError(String msgFormat, Object arg0) {
        return addError(StrUtil.format(of(msgFormat), arg0));
    }

    public ZRBuilder addError(String msgFormat, Object arg0, Object arg1) {
        return addError0(StrUtil.format(of(msgFormat), arg0, arg1));
    }

    public ZRBuilder addError(String msgFormat, Object arg0, Object arg1, Object arg2) {
        return addError0(StrUtil.format(of(msgFormat), arg0, arg1, arg2));
    }

    public ZRBuilder addError(String msgFormat, Object arg0, Object arg1, Object arg2, Object arg3) {
        return addError0(StrUtil.format(of(msgFormat), arg0, arg1, arg2, arg3));
    }

    public ZRBuilder addError(String msgFormat, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        return addError0(StrUtil.format(of(msgFormat), arg0, arg1, arg2, arg3, arg4));
    }

    public ZRBuilder addError(String msgFormat, Object... args) {
        return addError0(StrUtil.format(of(msgFormat), args));
    }

    private ZRBuilder addError0(String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            if (errors == null) { errors = new ArrayList<>(5); }
            errors.add(msg);
        }
        return this;
    }

    public ZRBuilder errorSeparator(String errorSeparator) {
        this.errorSeparator = errorSeparator;
        return this;
    }

    public boolean hasError() {
        return errors != null && !errors.isEmpty();
    }

    public ZResult build() {
        return new ZResult().setSuccess(getSuccess()).setStatus(status).setMsg(getMsg());
    }

    public <T> ZResp<T> buildResp() {
        return buildResp(null);
    }

    public <T> ZResp<T> buildResp(T data) {
        return new ZResp<T>().setSuccess(getSuccess()).setStatus(status).setMsg(getMsg()).setData(data);
    }

    public <T> ZListResp<T> buildListResp() {
        return buildListResp(null);
    }

    public <T> ZListResp<T> buildListResp(List<T> list) {
        if (list == null) { list = new ArrayList<>(); }
        return buildListResp(list, list.size());
    }

    public <T> ZListResp<T> buildListResp(List<T> list, int totalCount) {
        return buildListResp(list, totalCount, 1, totalCount);
    }

    public <T> ZListResp<T> buildListResp(List<T> list, int totalCount, int pageNumber, int pageSize) {
        return new ZListResp<T>().setSuccess(getSuccess()).setStatus(status).setMsg(getMsg()).setData(list)
                .setTotalCount(totalCount).setPage(pageNumber, pageSize);
    }

    public ZError buildError() {
        return buildError(true);
    }

    public ZError buildError(boolean noStack) {
        return buildError(noStack, null);
    }

    public ZError buildError(Throwable cause) {
        return buildError(false, cause);
    }

    private ZError buildError(boolean noStack, Throwable cause) {
        ZError error;
        if (noStack) {
            error = ZError.cloneInstance();
        } else {
            error = cause == null ? new ZError() : new ZError(cause);
            StackTraceElement[] stacks = error.getStackTrace();
            if (stacks.length > 1) { error.setStackTrace(Arrays.copyOfRange(stacks, 1, stacks.length)); }
        }
        return error.setSuccess(getSuccess(false)).setStatus(status).setMsg(getMsg());
    }

    protected boolean getSuccess() {
        return getSuccess(!hasError());
    }

    protected boolean getSuccess(boolean defValue) {
        return success == null ? defValue : success;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String buildErrorMsg() {
        return buildErrorMsg(errorSeparator);
    }

    public String buildErrorMsg(String separator) {
        if (!hasError()) {
            return "";
        } else {
            int size = 0;
            separator = StringUtils.defaultIfEmpty(separator, ",");
            if (StringUtils.isNotEmpty(module)) { size += module.length() + 1; }
            for (String value : errors) {
                size += StringUtils.length(value);
                size += separator.length();
            }
            TextStringBuilder sb = new TextStringBuilder(size);
            if (StringUtils.isNotEmpty(module)) { sb.append(module).append('-'); }
            boolean first = true;
            for (String value : errors) {
                if (first) {
                    sb.append(value);
                    first = false;
                } else {
                    sb.append(separator).append(value);
                }
            }
            return sb.toString();
        }
    }

    protected String getMsg() {
        if (StringUtils.isEmpty(msg)) {
            return hasError() ? buildErrorMsg() : StringUtils.defaultString(msg);
        } else {
            return msg;
        }
    }

    public static String of(String msg) {
        String newMsg = MessageUtil.get(NAMESPACE, msg);
        return StringUtils.defaultIfEmpty(newMsg, msg);
    }
}
