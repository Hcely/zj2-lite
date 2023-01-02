package org.zj2.lite.service.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *  PageRequest
 *
 * @author peijie.ye
 * @date 2022/11/25 9:56
 */
@Getter
@Setter
@Accessors(chain = true)
public class PageRequest implements Serializable {
    private static final long serialVersionUID = 2113452348160379344L;
    @Schema(description = "页码")
    protected Integer pageNumber;
    @Schema(description = "条数")
    protected Integer pageSize;
}
