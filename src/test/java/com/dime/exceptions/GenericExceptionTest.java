package com.dime.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class GenericExceptionTest {

    @Test
    void testGetMessage_with_arguments() {
        GenericErrorResponse errorResponse = GenericError.TERM_NOT_FOUND;
        Map<String, Object> messageArguments = new HashMap<>();
        messageArguments.put("id", 45);

        GenericException genericException = new GenericException(errorResponse, messageArguments);

        String result = genericException.getMessage();

        assertEquals("Term with id [45] not found", result);
    }
}
