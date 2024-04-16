package cn.woodwhales.ffmpeg.aop;

import cn.woodwhales.common.model.vo.RespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author woodwhales on 2023-03-23 9:14
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RespVO<Void> exception(Exception exception) {
        String errorMsg = exception.getMessage();
        if(exception instanceof NullPointerException) {
            errorMsg = "NPE异常";
        }
        log.error("errorMsg={}", errorMsg, exception);
        return RespVO.errorWithErrorMsg(errorMsg);
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public RespVO<Void> exception(MethodArgumentNotValidException exception) {
        String errorMsg = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        log.error("errorMsg={}", errorMsg, exception);
        return RespVO.errorWithErrorMsg(errorMsg);
    }

}
