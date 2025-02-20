package org.bsc.quarkus;

import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.FileSystemAccess;
import io.vertx.ext.web.handler.StaticHandler;
import jakarta.enterprise.event.Observes;


public class StaticResources {

    void installRoute(@Observes StartupEvent startupEvent, Router router) {
        router.route()
                .path("/*")
                .handler(StaticHandler.create(FileSystemAccess.RELATIVE,"webapp/"));
    }
}
