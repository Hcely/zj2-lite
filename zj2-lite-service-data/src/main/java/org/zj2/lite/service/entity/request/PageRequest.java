package org.zj2.lite.service.entity.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *  PageRequest
 *
 * @author peijie.ye
 * @date 2022/11/25 9:56
 */
@Getter
@Setter
@Accessors(chain = true)
public class PageRequest {
    protected Integer pageNumber;
    protected Integer pageSize;
}
