package cn.edu.csust.coursecard.exception;


import cn.edu.csust.coursecard.common.CodeEnum;
import cn.edu.csust.coursecard.common.ReturnData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 * @author zsw
 * @date 2019/6/21 16:07
 */
@RestControllerAdvice
@Slf4j
public class ExceptionResolver {
    /**
     * 捕获全局业务异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public ReturnData BaseExceptionHandler(BaseException e) {
        return ReturnData.fail(e.getCode(), e.getMsg());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ReturnData ExceptionHandler(Exception e) {
        e.printStackTrace();
        return ReturnData.fail(CodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = {ConstraintViolationException.class, BindException.class, ServletRequestBindingException.class, MethodArgumentNotValidException.class})
    public ReturnData ConstraintViolationExceptionHandler(Exception e) {
//        e.printStackTrace();
        log.error("-------------->参数异常 {}", e.getMessage());
        return ReturnData.fail(CodeEnum.REQUEST_FAILED.getCode(),"参数异常");
    }

    @ResponseBody
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ReturnData MethodNotSupportedExceptionHandler(Exception e) {
        return ReturnData.fail(CodeEnum.REQUEST_FAILED.getCode(),"请求方式不被允许");
    }
    @ResponseBody
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ReturnData NoHandlerFoundExceptionExceptionHandler(Exception e) {
        return ReturnData.fail(CodeEnum.URI_NOT_EXIST.getCode(),"访问的资源不存在，请检查访问路径");
    }
}
