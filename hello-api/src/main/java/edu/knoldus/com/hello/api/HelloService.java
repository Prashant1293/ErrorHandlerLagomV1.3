/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package edu.knoldus.com.hello.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.transport.Method.DELETE;
import static com.lightbend.lagom.javadsl.api.transport.Method.GET;
import static com.lightbend.lagom.javadsl.api.transport.Method.POST;
import static com.lightbend.lagom.javadsl.api.transport.Method.PUT;


/**
 * The Hello service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the Hello.
 */
public interface HelloService extends Service {
    
    /**
     * Example: curl http://localhost:9000/api/hello/Alice
     */
    //ServiceCall<NotUsed, String> hello(String id);
    
    ServiceCall<NotUsed, Optional<Employee>> getEmployee(String vendorId);
    
    ServiceCall<Employee, String> createEmployee();
    
    ServiceCall<NotUsed, String> deleteEmployee(String vendorId);
    
    ServiceCall<Employee, String> updateEmployee(String vendorId);
    
    
    /**
     * Example: curl -H "Content-Type: application/json" -X POST -d '{"message":
     * "Hi"}' http://localhost:9000/api/hello/Alice
     */
    //ServiceCall<GreetingMessage, Done> useGreeting(String id);
    
    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("hello")
                .withCalls(
                        restCall(GET, "/hello/employee/:vendorId", this::getEmployee),
                        restCall(POST, "/hello/employee/:vendorId", this::createEmployee),
                        restCall(DELETE, "/hello/employee/:vendorId", this::deleteEmployee),
                        restCall(PUT, "/hello/employee/:vendorId", this::updateEmployee)
                ).withAutoAcl(true);
        // @formatter:on
    }
}
