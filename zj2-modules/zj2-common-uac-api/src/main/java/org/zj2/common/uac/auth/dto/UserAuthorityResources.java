package org.zj2.common.uac.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 *  UserResources
 *
 * @author peijie.ye
 * @date 2023/1/2 19:22
 */
@Getter
@Setter
@NoArgsConstructor
public class UserAuthorityResources implements Serializable {
    private static final long serialVersionUID = 3292141917083713905L;
    private String userId;
    private Set<String> authorityResources;
}
