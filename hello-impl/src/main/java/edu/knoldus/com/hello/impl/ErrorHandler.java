package edu.knoldus.com.hello.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler {
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    @Inject
    public ErrorHandler(Config configuration, Environment environment,
                        OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);
    }
    
    @Override
    protected CompletionStage<Result> onNotFound(Http.RequestHeader request, String message) {
        String errorResponse = "The requested resource does not exist.";
        
        JsonNode jsonNode = OBJECT_MAPPER.valueToTree(errorResponse);
        
        return CompletableFuture.completedFuture(Results.notFound(jsonNode));
    }
}
