package org.zj2.lite.service.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 *  UserAuthorityResource
 *
 * @author peijie.ye
 * @date 2023/1/2 22:05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authority implements Serializable {
    private static final long serialVersionUID = 3432530857062182779L;
    private String name;
    private long expireTime;
}
