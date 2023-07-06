package cn.auto.exception;

import cn.auto.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/** 全局异常处理器
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServerException.class)
    public Result<Object> serverExceptionHandler(ServerException e){
        log.error("自定义服务异常",e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Object> exceptionHandler(Exception e){
        log.error("服务异常",e);
        return Result.fail(e.getMessage());
    }
}
