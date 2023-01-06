package org.zj2.common.uac.auth.authorize;

import org.zj2.lite.common.Supportable;

/**
 *  AuthorityPropertyHider
 *
 * @author peijie.ye
 * @date 2023/1/7 2:46
 */
public interface PropertyHideShredder extends Supportable<String> {
    String STAR_4_STR= "****";
    @Override
    boolean supports(String propertyName);

    String hide(String value);
}
