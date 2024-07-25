package com.dime.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class GenericExceptionTest {

    @Test
    void test_getMessage_with_arguments() {
        GenericException result = GenericError.TERM_NOT_FOUND.exWithArguments(Map.of("id", 45));

        assertEquals(GenericError.TERM_NOT_FOUND, result.getErrorResponse());
        assertEquals("Term with id [45] not found", result.getMessage());
        Map<String, Object> arguments = result.getMessageArguments();
        assertEquals(1, arguments.size());
        assertEquals(45, arguments.get("id"));
    }
}
