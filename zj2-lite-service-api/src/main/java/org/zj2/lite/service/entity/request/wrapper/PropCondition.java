package org.zj2.lite.service.entity.request.wrapper;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

/**
 * QueryCondition
 *
 * @author peijie.ye
 * @date 2022/11/27 4:13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropCondition implements Serializable {
    private static final long serialVersionUID = -2735104018784402245L;
    @JSONField(name = "n")
    @JsonProperty("n")
    private String name;
    @JSONField(name = "m")
    @JsonProperty("m")
    private WhereMode mode;
    @JSONField(name = "v1")
    @JsonProperty("v1")
    private Object value1;//NOSONAR
    @JSONField(name = "v2")
    @JsonProperty("v2")
    private Object value2;//NOSONAR
    @JSONField(name = "vs")
    @JsonProperty("vs")
    private Collection<Object> values;//NOSONAR
}
