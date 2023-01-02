package org.zj2.common.uac.app.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.service.entity.request.PageRequest;

/**
 *  AppUserQuery
 *
 * @author peijie.ye
 * @date 2023/1/2 18:17
 */
@Getter
@Setter
@NoArgsConstructor
public class AppUserQuery extends PageRequest {
    private static final long serialVersionUID = 3074366372946185647L;
}
