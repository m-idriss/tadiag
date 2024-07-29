package com.dime.exceptions;

import jakarta.ws.rs.core.Response.Status;
import java.util.Map;

public enum GenericError implements GenericErrorResponse {

    TERM_NOT_FOUND("ID_NOT_FOUND", Status.NOT_FOUND, "Term with id [{id}] not found"),
    TERM_CONFLICTS("ID_CONFLICT", Status.CONFLICT, "Term with id [{id}] make conflict"),
    WORD_NOT_FOUND("WORD_NOT_FOUND", Status.NOT_FOUND, "Word [{word}] do not exist"),
    FAILED_DEPENDENCY("FAILED_DEPENDENCY", Status.INTERNAL_SERVER_ERROR,
            "Failed during using dependency, [code : {code}]"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", Status.INTERNAL_SERVER_ERROR,
            Status.INTERNAL_SERVER_ERROR.getReasonPhrase());

    private final String key;
    private final Status httpStatus;
    private final String message;

    GenericError(String key, Status httpStatus, String message) {
        this.key = key;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Status getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public GenericException exWithArguments(Map<String, Object> arguments) {
        return new GenericException(this, arguments);
    }
}
