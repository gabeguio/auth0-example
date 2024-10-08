package com.example.helloworld.config;

import static org.springframework.web.servlet.function.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import com.example.helloworld.handlers.MessageHandler;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> apiRouter(
            final MessageHandler messageHandler,
            final GlobalErrorHandler globalErrorHandler) {
        final var api = Paths.apiPath();
        final var messages = api.messagesPath();

        return route()
                .path(api.segment(), () -> route()
                        .path(messages.segment(), () -> route()
                                .GET(messages.publicPath().segment(), messageHandler::getPublic)
                                .GET(messages.protectedPath().segment(), messageHandler::getProtected)
                                .GET(messages.adminPath().segment(), messageHandler::getAdmin)
                                .build())
                        .build())
                .onError(Throwable.class, globalErrorHandler::handleInternalError)
                .build();
    }
}
