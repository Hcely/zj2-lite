package org.zj2.lite.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.zj2.lite.common.CodeEnum;

import java.io.Serializable;

/**
 *  ZReference
 *
 * @author peijie.ye
 * @date 2022/11/29 14:37
 */
@Getter
public class ZReference implements CodeEnum<Integer>, Serializable {
    private static final long serialVersionUID = 20221129143709L;
    @JSONField
    @JsonProperty
    private Integer code;
    @JSONField
    @JsonProperty
    private String desc;

    protected ZReference() {
    }

    public ZReference(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
