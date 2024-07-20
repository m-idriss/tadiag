package com.dime.exceptions;

import java.util.Map;

public class GenericException extends RuntimeException {

    private static final long serialVersionUID = -3784903329806863768L;
    private final transient GenericErrorResponse errorResponse;
    private final transient Map<String, Object> messageArguments;

    public GenericException(GenericErrorResponse errorResponse, Map<String, Object> messageArguments) {
        this.errorResponse = errorResponse;
        this.messageArguments = messageArguments;
    }

    public GenericErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public Map<String, Object> getMessageArguments() {
        return messageArguments;
    }

    @Override
    // replace term in {} in message with messageArguments
    public String getMessage() {
        String message = errorResponse.getMessage();
        for (Map.Entry<String, Object> entry : messageArguments.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return message;
    }
}
