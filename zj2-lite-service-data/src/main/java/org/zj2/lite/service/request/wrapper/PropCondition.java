package org.zj2.lite.service.request.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

/**
 *  QueryCondition
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
    private String name;
    private WhereMode mode;
    private Object value1;//NOSONAR
    private Object value2;//NOSONAR
    private Collection<Object> values;//NOSONAR
}
