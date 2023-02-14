package org.zj2.lite.service.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.zj2.lite.service.context.TokenType;

import java.io.Serializable;
import java.util.Set;

/**
 * UriResource
 *
 * @author peijie.ye
 * @date 2023/2/7 10:47
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class UriResource implements Serializable {
    private static final long serialVersionUID = 4117537284962869204L;
    protected String name;
    protected String uriPath;
    protected boolean requiredAuthentication;
    protected TokenType[] requiredTokenTypes;
    protected boolean requiredUriAuthority;
    protected String uriAuthority;
    protected boolean requiredPropertyAuthority;
    protected Set<String> propertyAuthorities;//NOSONAR
    protected boolean requiredDataAuthority;
    protected Set<String> tags;//NOSONAR
    protected String dataAuthority;
}
