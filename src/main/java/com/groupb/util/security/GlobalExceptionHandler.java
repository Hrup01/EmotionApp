package com.groupb.util.security;


import com.groupb.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public Result handleAuthException(Exception ex) {
        log.warn("认证失败: {}", ex.getMessage());
        return Result.error("未认证或令牌无效");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDenied(AccessDeniedException ex) {
        log.warn("权限不足: {}", ex.getMessage());
        return Result.error("权限不足");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst().map(err -> err.getField() + ": " + err.getDefaultMessage()).orElse("参数校验失败");
        return Result.error(msg);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result handleOthers(Exception ex) {
        log.error("服务器异常", ex);
        return Result.error("服务器错误");
    }
}


