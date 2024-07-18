package com.dime.extensions.wordsapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.inject.Inject;

@ExtendWith(MockitoExtension.class)
class WordsApiHealthIndicatorTest {

    @RestClient
    @Inject
    @Mock
    private WordsApiService wordsApiService;

    @InjectMocks
    private WordsApiHealthIndicator wordsApiHealthIndicator;

    @Test
    void testCall_HealthApiUp() {
        when(wordsApiService.healthApi()).thenReturn("up");

        HealthCheckResponse response = wordsApiHealthIndicator.call();

        assertNotNull(response);
        assertEquals("Words API", response.getName());
        assertEquals(HealthCheckResponse.Status.UP, response.getStatus());
        assertEquals("up", response.getData().get().get("synonyms"));
        verify(wordsApiService, times(1)).healthApi();
    }

    @Test
    void testCall_HealthApiDown() {
        doThrow(new RuntimeException("API error")).when(wordsApiService).healthApi();

        HealthCheckResponse response = wordsApiHealthIndicator.call();

        assertNotNull(response);
        assertEquals("Words API", response.getName());
        assertEquals(HealthCheckResponse.Status.DOWN, response.getStatus());
        assertEquals("down", response.getData().get().get("synonyms"));
        verify(wordsApiService, times(1)).healthApi();
    }
}
