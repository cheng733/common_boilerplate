package com.common.exception;

import com.common.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Result<String> handleException(Exception e) {
    // 记录详细错误信息到日志
    logger.error("系统异常", e);
    // 返回统一的错误信息
    return Result.error(500, "系统内部错误");
  }

  @ExceptionHandler(RuntimeException.class)
  public Result<String> handleRuntimeException(RuntimeException e) {
    logger.error("运行时异常", e);
    return Result.error(500, "系统内部错误");
  }
}
