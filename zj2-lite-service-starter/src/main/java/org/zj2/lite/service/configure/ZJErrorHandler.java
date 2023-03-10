package org.zj2.lite.service.configure;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zj2.lite.common.entity.result.ZError;
import org.zj2.lite.common.entity.result.ZResult;
import org.zj2.lite.common.entity.result.ZStatusMsg;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.util.ZRBuilder;

import java.util.List;
import java.util.Set;

/**
 * ErrorHandler
 *
 * @author peijie.ye
 * @date 2022/12/20 21:41
 */
@Component
@ControllerAdvice
@Slf4j
public class ZJErrorHandler {
    @ExceptionHandler(ZError.class)
    @ResponseBody
    public ZResult error(ZError error) {
        if(error.isStack()) {
            log.error(error.toString(), error);
        } else {
            log.error(error.toString());
        }
        return new ZResult().setSuccess(error.isSuccess()).setStatus(error.getStatus()).setMsg(error.getMsg());
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ZResult validError(BindException exception) {
        Set<String> messages = CollUtil.toSet(exception.getBindingResult().getAllErrors(), DefaultMessageSourceResolvable::getDefaultMessage, true);
        ZMultiMsgResult result = new ZMultiMsgResult();
        result.setSuccess(false);
        result.setStatus(ZStatusMsg.FAILURE_STATUS);
        if(CollUtil.isNotEmpty(messages)) {
            result.setMsg(ZRBuilder.ofMsg(CollUtil.getFirst(messages)));
            result.setMsgs(CollUtil.toList(messages, ZRBuilder::ofMsg));
        } else {
            String msg = ZRBuilder.ofMsg("参数错误");
            result.setMsg(msg);
            result.setMsgs(CollUtil.singletonList(msg));
        }
        return result;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ZResult sysError(Throwable error) {
        log.error(error.toString(), error);
        return ZRBuilder.builder().success(false).status(ZStatusMsg.SYS_ERROR_STATUS).msg("系统异常").build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ZMultiMsgResult extends ZResult {
        private static final long serialVersionUID = 8695804418656551783L;
        private List<String> msgs;
    }
}
