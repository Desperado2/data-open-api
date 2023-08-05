package com.github.desperado2.data.open.api.common.manage.exception;

import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.common.manage.model.ExternalResult;
import com.sun.media.sound.InvalidFormatException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * 功能描述: 全局异常接收
 * @author corbett
 * @date 2018/9/6 13:41
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public DataResult<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.info("缺少请求参数={}", e.getMessage());
        e.printStackTrace();
        String parameterName = e.getParameterName();
        String errorMessage = String.format(ErrorCodeEnum.PARAMS_MISSING.getErrorMessage(), parameterName);
        return new DataResult<>(ErrorCodeEnum.PARAMS_MISSING.getHttpCode(),ErrorCodeEnum.PARAMS_MISSING.getInternalErrorCode(),errorMessage);
    }

    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public DataResult<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.info("参数解析失败={}", e.getMessage());
        e.printStackTrace();
        return new DataResult<>(ErrorCodeEnum.PARAMS_READABLE.getHttpCode(),ErrorCodeEnum.PARAMS_READABLE.getInternalErrorCode(),ErrorCodeEnum.PARAMS_READABLE.getErrorMessage());
    }


    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DataResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.info("参数验证失败={}",e.getMessage());
        e.printStackTrace();
        BindingResult result = e.getBindingResult();
        StringBuilder buf = new StringBuilder();
        List<FieldError> fieldErrors = result.getFieldErrors();
        IntStream.range(0, fieldErrors.size()).forEach((index) -> {
            if (index != 0) {
                buf.append(",");
            }
            FieldError info = fieldErrors.get(index);
            String message = info.getDefaultMessage();
            if (StringUtils.isNotBlank(message) && info.getRejectedValue() != null) {
                message = message.replace("{}", info.getRejectedValue().toString());
            }
            buf.append(message);
        });
        return new DataResult<>(ErrorCodeEnum.PARAMS_ERROR.getHttpCode(), ErrorCodeEnum.PARAMS_ERROR.getInternalErrorCode(),buf.toString());
    }

    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(BindException.class)
    public DataResult<String> handleBindException(BindException e) {
        logger.info("参数绑定失败={}", e.getMessage());
        e.printStackTrace();
        BindingResult result = e.getBindingResult();
        StringBuilder buf = new StringBuilder();
        List<FieldError> fieldErrors = result.getFieldErrors();
        IntStream.range(0, fieldErrors.size()).forEach((index) -> {
            if (index != 0) {
                buf.append(",");
            }
            FieldError info = fieldErrors.get(index);
            String message = info.getDefaultMessage();
            if (StringUtils.isNotBlank(message) && info.getRejectedValue() != null) {
                message = message.replace("{}", info.getRejectedValue().toString());
            }
            buf.append(message);
        });
        return new DataResult<>(ErrorCodeEnum.PARAMS_BIND.getHttpCode(), ErrorCodeEnum.PARAMS_BIND.getInternalErrorCode(),buf.toString());
    }



    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public DataResult<String> handleServiceException(ConstraintViolationException e) {
        logger.info("参数验证失败={}", e.getMessage());
        e.printStackTrace();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = violations.iterator();
        StringBuilder message = new StringBuilder();
        while (iterator.hasNext()){
            ConstraintViolation<?> violation = iterator.next();
            //PathImpl propertyPath = (PathImpl)(violation.getPropertyPath());
            //String name = propertyPath.getLeafNode().getName();
            message.append(violation.getMessage()).append(",");
        }
        return new DataResult<>(ErrorCodeEnum.PARAMS_CONSTRAINT.getHttpCode(), ErrorCodeEnum.PARAMS_CONSTRAINT.getInternalErrorCode(), message.toString());
    }

    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(ValidationException.class)
    public DataResult<String> handleValidationException(ValidationException e) {
        logger.info("参数验证失败={}", e.getMessage());
        e.printStackTrace();
        String errorMsg = e.getCause().getLocalizedMessage() == null ? e.getMessage() : e.getCause().getLocalizedMessage();
        return new DataResult<>(ErrorCodeEnum.PARAMS_VALIDATION.getHttpCode(),ErrorCodeEnum.PARAMS_VALIDATION.getInternalErrorCode(),errorMsg);
    }

    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(InvalidFormatException.class)
    public DataResult<String> invalidFormatException(InvalidFormatException e) {
        logger.info("参数格式化失败={}", e.getMessage());
        e.printStackTrace();
        String errorMsg = e.getCause().getLocalizedMessage() == null ? e.getMessage() : e.getCause().getLocalizedMessage();
        return new DataResult<>(ErrorCodeEnum.PARAMS_FORMAT.getHttpCode(),ErrorCodeEnum.PARAMS_FORMAT.getInternalErrorCode(),errorMsg);
    }


    /**
     * 405 - Method Not Allowed
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public DataResult<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.info("不支持当前请求方法={}", e.getMessage());
        e.printStackTrace();
        return new DataResult<>(ErrorCodeEnum.REQUEST_NOT_ALLOW.getHttpCode(),ErrorCodeEnum.REQUEST_NOT_ALLOW.getInternalErrorCode(),ErrorCodeEnum.REQUEST_NOT_ALLOW.getErrorMessage());
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public DataResult<String> handleHttpMediaTypeNotSupportedException(Exception e) {
        logger.info("不支持当前媒体类型={}", e.getMessage());
        e.printStackTrace();
        return new DataResult<>(ErrorCodeEnum.UNSUPPORTED_MEDIA_TYPE.getHttpCode(),ErrorCodeEnum.UNSUPPORTED_MEDIA_TYPE.getInternalErrorCode(),ErrorCodeEnum.UNSUPPORTED_MEDIA_TYPE.getErrorMessage());
    }

    /**
     * 500 - Internal Server info
     */
    @ExceptionHandler(Exception.class)
    public DataResult<String> handleException(Exception e) {
        logger.info("系统异常={}", e.getLocalizedMessage());
        e.printStackTrace();
        return new DataResult<>(ErrorCodeEnum.SERVICE_ERROR.getHttpCode(),ErrorCodeEnum.SERVICE_ERROR.getInternalErrorCode(),ErrorCodeEnum.SERVICE_ERROR.getErrorMessage());

    }




    /**
     * 自定义异常
     */
    @ExceptionHandler(DataOpenPlatformException.class)
    public DataResult<String> skfSuperCodeException(DataOpenPlatformException e) {
        logger.info("自义定异常={}" ,e.getClass().getName());
        e.printStackTrace();
        return new DataResult<>(ErrorCodeEnum.SERVICE_ERROR.getHttpCode(),e.getInternalErrorCode(),e.getMessage());
    }


    /**
     * 拦截Assert抛出的异常
     * @param e 异常
     * @return 前端返回值
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public DataResult<String> illegalArgumentException(IllegalArgumentException e) {
        logger.info("自义定异常={}" ,e.getClass().getName());
        e.printStackTrace();
        return new DataResult<>(ErrorCodeEnum.SERVICE_ERROR.getHttpCode(),ErrorCodeEnum.SERVICE_ERROR.getInternalErrorCode(),e.getMessage());
    }



    /**
     * 对外接口自定义异常
     * @param e 异常
     * @return 返回值
     */
    @ExceptionHandler(ExternalException.class)
    public ExternalResult<String> externalException(ExternalException e) {
        logger.info("对外接口自义定异={}" , e.getClass().getName());
        e.printStackTrace();
        return new ExternalResult<String>(e.getReturnCode(),e.getMessage(),null);
    }

}
