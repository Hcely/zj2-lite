package org.zj2.lite.service.configure.logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.entity.result.ZStatusMsg;

/**
 *  ZJRequestLogFilter
 *
 * @author peijie.ye
 * @date 2022/12/27 22:06
 */
@Component
@Order(-10000)
public class AbstractRequestLogFilter {
    private static final long SHOW_RESPONSE_THRESHOLD = 5000;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected void logRequest(RequestLogContext context) {
        StringBuilder sb = new StringBuilder(192);
        sb.append(context.getRpc());
        if (StringUtils.isEmpty(context.getMethod())) {
            sb.append(" REQ-");
        } else {
            sb.append(" REQ [").append(context.getMethod()).append("]-");
        }
        sb.append(context.getUri());
        String message = sb.toString();
        log.info(message);
    }

    protected void logResponse(RequestLogContext context) {//NOSONAR
        long take = context.getEndTime() - context.getStartTime();
        long executeTake = context.getExecuteEndTime() - context.getExecuteStartTime();
        TextStringBuilder sb = new TextStringBuilder(256);
        sb.append(context.getRpc());
        if (StringUtils.isEmpty(context.getMethod())) {
            sb.append(" RESP-");
        } else {
            sb.append(" RESP [").append(context.getMethod()).append("]-");
        }
        sb.append(context.getUri()).append('(').append(take).append("ms");
        if (context.isExecuted()) {
            sb.append(",exe ").append(executeTake);
            sb.append(executeTake > SHOW_RESPONSE_THRESHOLD ? "ms SLOW)" : "ms)");
        } else {
            sb.append(')');
        }
        sb.append(",resp:").append(context.getResponseStatus());
        final Object result = context.getResponse();
        final Throwable error = context.getError();
        final Object[] params = context.getRequestParams();
        boolean resultError = false;
        if (error != null) {
            if (error instanceof ZStatusMsg) {
                appendStatusMsg(sb, (ZStatusMsg) error);
            } else {
                resultError = true;
                appendParams(sb, params);
            }
        } else if (result == null) {
            sb.append(",result:null");
        } else if (result instanceof ZStatusMsg) {
            appendStatusMsg(sb, (ZStatusMsg) result);
        } else if (BeanUtils.isSimpleValueType(result.getClass())) {
            sb.append(",result:").append(result);
        } else if (result instanceof ResponseEntity) {
            sb.append(",result:ResponseEntity");
        } else {
            sb.append(",result:unknown(").append(result.getClass().getSimpleName()).append(')');
        }
        String message = sb.toString();
        if (resultError) {
            log.error(message, error);
        } else {
            log.info(message);
        }
    }

    private void appendStatusMsg(TextStringBuilder sb, ZStatusMsg statusMsg) {
        sb.append(",success:").append(statusMsg.isSuccess());
        sb.append(",status:").append(statusMsg.getStatus());
        sb.append(",msg:").append(statusMsg.getMsg());
    }

    private void appendParams(TextStringBuilder sb, Object[] params) {
        sb.append(",reqParams:[");
        if (params != null && params.length > 0) {
            int i = 0;
            sb.ensureCapacity(1024 * 4);
            for (Object arg : params) {
                if (++i > 1) {sb.append(',');}
                sb.append(arg);
            }
        }
        sb.append(']');
    }
}
