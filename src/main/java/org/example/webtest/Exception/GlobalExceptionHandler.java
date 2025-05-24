package org.example.webtest.Exception;

import org.example.webtest.Utils.APIResponsePacker;
import org.example.webtest.Utils.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public APIResponsePacker<Void> handleBusinessException(BusinessException e) {
        logger.error("Business error: {}", e.getMessage());
        return new APIResponsePacker<>(e.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public APIResponsePacker<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        logger.error("File size exceeds limit: {}", e.getMessage());
        return new APIResponsePacker<>(ResultCode.FILE_SIZE_ERROR.getCode(), 
            ResultCode.FILE_SIZE_ERROR.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public APIResponsePacker<Void> handleException(Exception e) {
        logger.error("System error: ", e);
        return new APIResponsePacker<>(ResultCode.SYSTEM_ERROR.getCode(), 
            ResultCode.SYSTEM_ERROR.getMessage(), null);
    }
} 