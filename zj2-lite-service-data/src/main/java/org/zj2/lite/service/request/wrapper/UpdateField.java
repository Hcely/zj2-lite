package org.zj2.lite.service.request.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 *  UpdateField
 *
 * @author peijie.ye
 * @date 2022/11/27 15:44
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateField implements Serializable {
    private static final long serialVersionUID = 3798121877468231721L;
    private String name;
    private UpdateMode mode;
    private Object value;//NOSONAR
}
