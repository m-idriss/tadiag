package com.dime;

import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import jakarta.enterprise.event.Observes;

public class StaticResources {

    /*
     * This method is annotated with @Observes to indicate that it should be called
     * when the application starts. The method installs a route that serves static
     * resources from the static folder.
     */
    void installRoute(@Observes StartupEvent startupEvent, Router router) {
        router.route()
                .path("/static/*")
                .handler(StaticHandler.create("static/"));
    }

    /*
     * This method is annotated with @Observes to indicate that it should be called
     * when the application starts. The method installs a route that redirects the
     * / path to /static/index.html.
     */
    void redirectToIndex(@Observes StartupEvent startupEvent, Router router) {
        router.route().path("/").handler(ctx -> ctx.response().putHeader("location", "/static/index.html")
                .setStatusCode(302).end());
    }

    /*
     * This method is annotated with @Observes to indicate that it should be called
     * when the application starts. The method installs a route that redirects the
     * /api path to /swagger-ui.
     */
    void redirectToSwagger(@Observes StartupEvent startupEvent, Router router) {
        router.route().path("/api").handler(ctx -> ctx.response().putHeader("location", "/swagger-ui")
                .setStatusCode(302).end());
    }

}
