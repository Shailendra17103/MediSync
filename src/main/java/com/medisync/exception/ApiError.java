package com.medisync.exception;

import java.time.Instant;
import java.util.Map;

public class ApiError {
    public Instant timestamp = Instant.now();
    public int status;
    public String error;
    public String message;
    public String path;
    public Map<String, String> fieldErrors; // optional: for validation errors

    public ApiError(int status, String error, String message, String path, Map<String, String> fieldErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }
}