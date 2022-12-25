package org.zj2.lite.service.configure;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

import java.util.List;
import java.util.Set;

/**
 *  ErrorHandler
 *
 * @author peijie.ye
 * @date 2022/12/20 21:41
 */
//@Component
//@ControllerAdvice(value = "org.zj2")
@Slf4j
public class ZJErrorHandler {
    @ExceptionHandler(ZError.class)
    @ResponseBody
    public ZResult zerror(ZError error) {
        log.error(error.toString(), error);
        return new ZResult().setSuccess(error.isSuccess()).setStatus(error.getStatus()).setMsg(error.getMsg());
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ZResult validError(BindException exception) {
        Set<String> messages = CollUtil.toSet(exception.getBindingResult().getAllErrors(),
                DefaultMessageSourceResolvable::getDefaultMessage, true);
        return new ZResult().setSuccess(false).setStatus(ZStatusMsg.FAILURE_STATUS)
                .setMsg(StringUtils.join(messages, ';'));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ZResult sysError(Exception error) {
        log.error(error.toString(), error);
        return new ZResult().setSuccess(false).setStatus(ZStatusMsg.SYS_ERROR_STATUS).setMsg("系统异常");
    }
}
