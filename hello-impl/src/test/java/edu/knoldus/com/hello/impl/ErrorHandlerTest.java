package edu.knoldus.com.hello.impl;

import org.junit.Test;
import org.mockito.Mockito;
import play.Configuration;
import play.Environment;
import play.api.mvc.RequestHeader;
import play.http.HttpEntity;
import play.mvc.Http;
import play.mvc.Result;

import static org.junit.Assert.assertTrue;

public class ErrorHandlerTest {
    
    private final ErrorHandler errorHandler = new ErrorHandler(Configuration.empty(),
            Environment.simple(), null, null);
    
    RequestHeader header = Mockito.mock(RequestHeader.class);
    private final Http.RequestHeader requestHeader = new Http.RequestImpl(header);
    
    @Test
    public void testOnNotFound() {
        Result result = errorHandler
                .onNotFound(requestHeader, "error")
                .toCompletableFuture()
                .join();
        
        String content = ((HttpEntity.Strict) result.body()).data().utf8String();
        
        assertTrue(content.contains("The requested resource does not exist."));
    }
}