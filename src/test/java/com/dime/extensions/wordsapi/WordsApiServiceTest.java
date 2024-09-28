package com.dime.extensions.wordsapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.dime.term.Term;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

public class WordsApiServiceTest {

    @Mock
    @RestClient
    @Inject
    WordsApiService wordsApiService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByWord() {
        // Arrange
        String word = "example";
        Term expectedTerm = new Term();
        expectedTerm.setWord(word);
        expectedTerm.setSynonyms(List.of("sample", "illustration"));

        when(wordsApiService.findByWord(word)).thenReturn(expectedTerm);

        // Act
        Term actualTerm = wordsApiService.findByWord(word);

        // Assert
        assertEquals(expectedTerm, actualTerm);
        verify(wordsApiService).findByWord(word);
    }

    @Test
    public void testHealthApi() {
        // Arrange
        String expectedHealthResponse = "OK";

        when(wordsApiService.healthApi()).thenReturn(expectedHealthResponse);

        // Act
        String actualHealthResponse = wordsApiService.healthApi();

        // Assert
        assertEquals(expectedHealthResponse, actualHealthResponse);
        verify(wordsApiService).healthApi();
    }

    @Test
    public void testToException_401Response() {
        // Arrange
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(401);
        when(response.getStatusInfo()).thenReturn(Response.Status.UNAUTHORIZED);

        // Act
        RuntimeException exception = WordsApiService.toException(response);

        // Assert
        assertNotNull(exception);
        assertEquals("Failed during using dependency, [code : 401]",
                exception.getMessage());
    }

    @Test
    public void testToException_403Response() {
        // Arrange
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(403);
        when(response.getStatusInfo()).thenReturn(Response.Status.FORBIDDEN);

        // Act
        RuntimeException exception = WordsApiService.toException(response);

        // Assert
        assertNotNull(exception);

        assertEquals("Failed during using dependency, [code : 403]",
                exception.getMessage());
    }

    @Test
    public void testToException_404Response() {
        // Arrange
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(404);
        when(response.getStatusInfo()).thenReturn(Response.Status.NOT_FOUND);

        // Act
        RuntimeException exception = WordsApiService.toException(response);

        // Assert
        assertNotNull(exception);
        assertEquals("Not Found",
                exception.getMessage());
    }

    @Test
    public void testToException_500Response() {
        // Arrange
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(500);
        when(response.getStatusInfo()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR);

        // Act
        RuntimeException exception = WordsApiService.toException(response);

        // Assert
        assertNotNull(exception);
        assertEquals("Failed during using dependency, [code : 500]",
                exception.getMessage());

    }
}
