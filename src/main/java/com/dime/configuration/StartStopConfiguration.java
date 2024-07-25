package com.dime.configuration;

import java.util.List;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.logging.Log;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.config.SmallRyeConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * This class is responsible for displaying a message in the console when the
 * application starts and stops.
 * Displays the following information in the console:
 * - Name, Version, Profiles used
 * - Listening on, Swagger UI, Health UI, Dev UI URLs
 */
@ApplicationScoped
public class StartStopConfiguration {

  /**
   * This method is called when the application starts.
   * It gets the configuration and builds the start message.
   * 
   * @param ev the startup event
   */
  void onStart(@Observes StartupEvent ev) {
    Config config = ConfigProvider.getConfig();
    buildStartMessageConsole(config);
  }

  /**
   * This method builds the start message that is displayed in the console when
   * the application starts.
   * It gets the application name, version, profiles, listening on, swagger ui,
   * health ui, dev ui URLs and displays them in the console.
   * 
   * @param config the application configuration
   */
  protected void buildStartMessageConsole(Config config) {
    String appName = config.getValue("quarkus.application.name", String.class);
    String version = config.getValue("quarkus.application.version", String.class);
    List<String> profile = config.unwrap(SmallRyeConfig.class).getProfiles();
    String listeningOn = config.getValue("quarkus.http.host", String.class) + ":"
        + config.getValue("quarkus.http.port", String.class);

    String protocol = "http";
    try {
      if (config.getOptionalValue("quarkus.http.ssl", Boolean.class).orElse(false)) {
        protocol = "https";
      }
    } catch (Exception e) {
      Log.info("Error getting protocol from config: " + e.getMessage());
    }

    String domain = protocol + "://" + listeningOn;

    String healthPath = domain
        + config.getOptionalValue("quarkus.smallrye-health.ui.root-path", String.class)
            .orElse("/health-ui");

    String devUiPath = domain + config.getOptionalValue("quarkus.smallrye-openapi.root-path", String.class)
        .orElse("/dev-ui");

    String swaggerPath = domain + config.getOptionalValue("quarkus.swagger-ui.path", String.class)
        .orElse("/swagger-ui");

    Log.infof(
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
        appName, profile, version, domain, swaggerPath, healthPath, devUiPath);
  }

  /**
   * This method is called when the application stops.
   * It displays a message in the console.
   * 
   * @param ev the shutdown event
   */
  void onStop(@Observes ShutdownEvent ev) {
    Log.info(
        "\n" +
            "\n+---+--------------------------------------------------------------------------" +
            "\n|   |                ... The application is stopping ... " +
            "\n+---+--------------------------------------------------------------------------");
  }
}
