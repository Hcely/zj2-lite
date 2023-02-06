package org.zj2.lite.service.configure.context;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.context.ZContexts;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 *  WebContextFilter
 *
 * @author peijie.ye
 * @date 2022/12/27 22:26
 */
@Component
@Order(-99999)
public class WebContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            ZContexts.clearContext();
        }
    }
}
