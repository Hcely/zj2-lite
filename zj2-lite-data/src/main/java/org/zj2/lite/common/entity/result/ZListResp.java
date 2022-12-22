package org.zj2.lite.common.entity.result;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  ZListResponse
 *
 * @author peijie.ye
 * @date 2022/11/23 9:50
 */
@Getter
@Setter
@Accessors(chain = true)
public class ZListResp<T> extends ZResp<List<? extends T>> {
    private static final long serialVersionUID = -277595428891369304L;
    protected int totalCount;
    protected int pageNumber;
    protected int pageSize;
    //
    protected Map<String, BigDecimal> sumValues;//NOSONAR

    public ZListResp<T> setPage(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        return this;
    }

    public ZListResp<T> putSumValue(String key, BigDecimal value) {
        if (sumValues == null) {sumValues = new LinkedHashMap<>();}
        sumValues.put(key, value);
        return this;
    }

    @Override
    public ZListResp<T> setData(List<? extends T> data) {
        super.setData(data);
        return this;
    }


    @Override
    public ZListResp<T> setStatus(int status) {
        super.setStatus(status);
        return this;
    }

    @Override
    public ZListResp<T> setSuccess(boolean success) {
        super.setSuccess(success);
        return this;
    }

    @Override
    public ZListResp<T> setMsg(String msg) {
        super.setMsg(msg);
        return this;
    }

    @Override
    public ZListResp<T> setMsg(String msgFormat, Object arg0) {
        super.setMsg(msgFormat, arg0);
        return this;
    }

    @Override
    public ZListResp<T> setMsg(String msgFormat, Object arg0, Object arg1) {
        super.setMsg(msgFormat, arg0, arg1);
        return this;
    }

    @Override
    public ZListResp<T> setMsg(String msgFormat, Object arg0, Object arg1, Object arg2) {
        super.setMsg(msgFormat, arg0, arg1, arg2);
        return this;
    }

    @Override
    public ZListResp<T> setMsg(String msgFormat, Object arg0, Object arg1, Object arg2, Object arg3) {
        super.setMsg(msgFormat, arg0, arg1, arg2, arg3);
        return this;
    }

    @Override
    public ZListResp<T> setMsg(String msgFormat, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        super.setMsg(msgFormat, arg0, arg1, arg2, arg3, arg4);
        return this;
    }

    @Override
    public ZListResp<T> setMsg(String msgFormat, Object... args) {
        super.setMsg(msgFormat, args);
        return this;
    }
}
