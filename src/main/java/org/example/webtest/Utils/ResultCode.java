package org.example.webtest.Utils;

public enum ResultCode {
    SUCCESS(0, "Success"),
    SYSTEM_ERROR(10000, "System error"),
    PARAM_ERROR(10001, "Parameter error"),
    FILE_TYPE_ERROR(10002, "Invalid file type"),
    FILE_SIZE_ERROR(10003, "File size exceeds limit"),
    FILE_UPLOAD_ERROR(10004, "File upload failed"),
    FILE_NOT_FOUND(10005, "File not found");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 