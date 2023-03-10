package org.zj2.common.uac.app.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.service.entity.request.PageRequest;

/**
 * AppQuery
 *
 * @author peijie.ye
 * @date 2023/1/2 18:16
 */
@Getter
@Setter
@NoArgsConstructor
public class AppQuery extends PageRequest {
    private static final long serialVersionUID = 4781376581160235073L;
    private String appName;
    private String appCode;
}
