package com.dime;

import java.util.List;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.logging.Log;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.config.SmallRyeConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class App {

    void onStart(@Observes StartupEvent ev) {

        Config config = ConfigProvider.getConfig();
        String appName = config.getValue("quarkus.application.name", String.class);
        String version = config.getValue("quarkus.application.version", String.class);
        List<String> profile = config.unwrap(SmallRyeConfig.class).getProfiles();
        String listeningOn = config.getValue("quarkus.http.host", String.class) + ":"
                + config.getValue("quarkus.http.port", String.class);

        String protocol = null;
        try {
            protocol = config.getValue("quarkus.http.ssl", String.class);
        } catch (Exception e) {
            Log.info("Error getting protocol from config: " + e.getMessage());
        }
        protocol = protocol != null ? "https" : "http";

        String domain = protocol + "://" + listeningOn;

        String healthPath = domain + config.getOptionalValue("quarkus.smallrye-health.ui.root-path", String.class)
                .orElse("/health-ui");

        // get dev ui path or /q/dev
        String devUiPath = domain + config.getOptionalValue("quarkus.smallrye-openapi.root-path", String.class)
                .orElse("/dev-ui");

        String sawggerPath = domain + config.getOptionalValue("quarkus.swagger-ui.path", String.class)
                .orElse("/swagger-ui");

        Log.infof(
                "\n" +
                        "\n+---+-------------------------------------------------------------------------" +
                        "\n|   | \t\t\tWelcome to %s !" +
                        "\n|   | " +
                        "\n|   | - Profiles \t: %s " +
                        "\n|   | - Version \t: %s " +
                        "\n|   | - Listening on \t: %s " +
                        "\n|   | - Api, Swagger UI : %s " +
                        "\n|   | - Health UI \t: %s " +
                        "\n|   | - Dev UI \t\t: %s " +
                        "\n|   | " +
                        "\n+---+-------------------------------[ Enjoy ]---------------------------------",
                appName, profile, version, domain, sawggerPath, healthPath, devUiPath);
    }

    void onStop(@Observes ShutdownEvent ev) {
        Log.info("The application is stopping...");
    }
}
