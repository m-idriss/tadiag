package com.dime.extensions.wordsapi;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Liveness
@ApplicationScoped
public class WordsApiHealthIndicator implements HealthCheck {

    @Inject
    @RestClient
    private WordsApiService wordsApiService;

    private static final Logger log = LoggerFactory.getLogger(WordsApiHealthIndicator.class);

    @Override
    public HealthCheckResponse call() {
        try {
            log.info("Performing Words API connection test...");
            wordsApiService.healthApi();
            log.info("Words API connection test successful.");
            return HealthCheckResponse.named("Words API")
                    .up()
                    .withData("synonyms", "up")
                    .build();
        } catch (Exception e) {
            log.error("Unexpected error during Words API connection test", e);
            return HealthCheckResponse.named("Words API")
                    .down()
                    .withData("synonyms", "down")
                    .build();
        }
    }
}
