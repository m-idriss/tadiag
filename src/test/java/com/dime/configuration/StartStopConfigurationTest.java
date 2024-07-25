package com.dime.configuration;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.quarkus.logging.Log;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.config.SmallRyeConfig;

@ExtendWith(MockitoExtension.class)
public class StartStopConfigurationTest {

    @Mock
    Config config;

    @Mock
    SmallRyeConfig smallRyeConfig;

    @InjectMocks
    StartStopConfiguration startStopConfiguration;

    @Captor
    ArgumentCaptor<Object[]> logCaptor;

    @Test
    void test_StartForNormalCase() {
        createConfig();
        try (MockedStatic<ConfigProvider> configProviderMock = Mockito.mockStatic(ConfigProvider.class)) {
            configProviderMock.when(ConfigProvider::getConfig).thenReturn(config);
            try (MockedStatic<Log> logMock = Mockito.mockStatic(Log.class)) {
                startStopConfiguration.onStart(new StartupEvent());

                logMock.verify(() -> Log.infof(
                        "\n" +
                                "\n+---+--------------------------------------------------------------------------" +
                                "\n|   | \t\t\tWelcome to %s !" +
                                "\n|   | " +
                                "\n|   | - Profiles \t: %s " +
                                "\n|   | - Version \t: %s " +
                                "\n|   | - Listening on \t: %s " +
                                "\n|   | - Api, Swagger UI : %s " +
                                "\n|   | - Health UI \t: %s " +
                                "\n|   | - Dev UI \t\t: %s " +
                                "\n|   | " +
                                "\n+---+-------------------------------[ Enjoy ]----------------------------------",
                        "TestApp", Arrays.asList("dev", "test"), "1.0.0", "http://localhost:8080",
                        "http://localhost:8080/swagger-ui", "http://localhost:8080/health-ui",
                        "http://localhost:8080/dev-ui"));
            }
        }
    }

    @Test
    void test_StartForExceptionCase() {
        createConfig();
        when(config.getOptionalValue("quarkus.http.ssl", Boolean.class))
                .thenThrow(new RuntimeException("Test Exception"));
        try (MockedStatic<ConfigProvider> configProviderMock = Mockito.mockStatic(ConfigProvider.class)) {
            configProviderMock.when(ConfigProvider::getConfig).thenReturn(config);
            try (MockedStatic<Log> logMock = Mockito.mockStatic(Log.class)) {
                startStopConfiguration.onStart(new StartupEvent());

                logMock.verify(() -> Log.info("Error getting protocol from config: Test Exception"));
            }
        }
    }

    @Test
    void test_StartForHttps() {
        createConfig();
        when(config.getOptionalValue("quarkus.http.ssl", Boolean.class)).thenReturn(java.util.Optional.of(true));
        try (MockedStatic<ConfigProvider> configProviderMock = Mockito.mockStatic(ConfigProvider.class);
                MockedStatic<Log> logMock = Mockito.mockStatic(Log.class)) {

            configProviderMock.when(ConfigProvider::getConfig).thenReturn(config);

            // Call the method to trigger log messages
            startStopConfiguration.onStart(new StartupEvent());

            // Capture the arguments passed to Log.infof
            ArgumentCaptor<String> logMessageCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Object[]> logArgsCaptor = ArgumentCaptor.forClass(Object[].class);
            logMock.verify(() -> Log.infof(logMessageCaptor.capture(), logArgsCaptor.capture()));

            // Retrieve the captured log message and arguments
            String logMessage = logMessageCaptor.getValue();
            Object[] logArgs = logArgsCaptor.getValue();
            System.out.println(logMessage);
            // logArgs to list String in stream
            String[] logArgsString = Arrays.stream(logArgs).map(Object::toString).toArray(String[]::new);
            assert (List.of(logArgsString).contains("https://localhost:8080"));
            assert (logMessage.contains("| - Listening on "));

        }
    }

    @Test
    void test_Stop() {
        try (MockedStatic<Log> logMock = Mockito.mockStatic(Log.class)) {
            startStopConfiguration.onStop(new ShutdownEvent());

            logMock.verify(
                    () -> Log.info("\n" +
                            "\n+---+--------------------------------------------------------------------------" +
                            "\n|   |                ... The application is stopping ... " +
                            "\n+---+--------------------------------------------------------------------------"));
        }
    }

    private void createConfig() {
        when(config.unwrap(SmallRyeConfig.class)).thenReturn(smallRyeConfig);
        when(config.getValue("quarkus.application.name", String.class)).thenReturn("TestApp");
        when(config.getValue("quarkus.application.version", String.class)).thenReturn("1.0.0");
        when(smallRyeConfig.getProfiles()).thenReturn(Arrays.asList("dev", "test"));
        when(config.getValue("quarkus.http.host", String.class)).thenReturn("localhost");
        when(config.getValue("quarkus.http.port", String.class)).thenReturn("8080");
        when(config.getOptionalValue("quarkus.http.ssl", Boolean.class)).thenReturn(java.util.Optional.of(false));
        when(config.getOptionalValue("quarkus.smallrye-health.ui.root-path", String.class))
                .thenReturn(java.util.Optional.of("/health-ui"));
        when(config.getOptionalValue("quarkus.smallrye-openapi.root-path", String.class))
                .thenReturn(java.util.Optional.of("/dev-ui"));
        when(config.getOptionalValue("quarkus.swagger-ui.path", String.class))
                .thenReturn(java.util.Optional.of("/swagger-ui"));
    }
}
