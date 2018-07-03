/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package edu.knoldus.com.hello.impl;

import akka.NotUsed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import edu.knoldus.com.hello.api.Employee;
import edu.knoldus.com.hello.api.HelloService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Implementation of the HelloService.
 */
public class HelloServiceImpl implements HelloService {
    
    //private final PersistentEntityRegistry persistentEntityRegistry;
    
    private static List<Employee> employee = new ArrayList<>();
    private final CassandraSession cassandraSession;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    @Inject
    public HelloServiceImpl(CassandraSession cassandraSession) {
        this.cassandraSession = cassandraSession;
    }
    /*@Inject
    public HelloServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        persistentEntityRegistry.register(HelloEntity.class);
    }*/
    
    /*@Override
    public ServiceCall<NotUsed, String> hello(String id) {
        return request -> {
            // Look up the hello world entity for the given ID.
            PersistentEntityRef<HelloCommand> ref = persistentEntityRegistry.refFor(HelloEntity.class, id);
            // Ask the entity the Hello command.
            return ref.ask(new Hello(id, Optional.empty()));
        };
    }
    
    @Override
    public ServiceCall<GreetingMessage, Done> useGreeting(String id) {
        return request -> {
            // Look up the hello world entity for the given ID.
            PersistentEntityRef<HelloCommand> ref = persistentEntityRegistry.refFor(HelloEntity.class, id);
            // Tell the entity to use the greeting message specified.
            return ref.ask(new UseGreetingMessage(request.message));
        };
        
    }*/
    
    @Override
    public ServiceCall<NotUsed, Optional<Employee>> getEmployee(String vendorID) {
        return req -> CompletableFuture.completedFuture(employee.stream()
                .filter(employee1 -> employee1.getVendorId().equalsIgnoreCase(vendorID))
                .findFirst());
    }
    
    @Override
    public ServiceCall<Employee, String> createEmployee() {
        
        return req -> {
            employee.add(req);
            int id = 0;
            try {
                id = OBJECT_MAPPER.readValue(req.getVendorId(), Integer.class);
            } catch (IOException ex) {
                System.out.println("can't transform ");
            }
            return cassandraSession.executeWrite("insert into user.userinfo(name, age) values(?,?)",
                    req.getName(), id)
                    .thenApply(notUsed -> "Successfully Inserted.")
                    .exceptionally(throwable -> {
                        Throwable cause = throwable.getCause();
                        System.out.println(cause);
                        throw new RuntimeException();
                    });
            //return CompletableFuture.completedFuture("Successfully Inserted.");
        };
    }
    
    @Override
    public ServiceCall<NotUsed, String> deleteEmployee(String vendorId) {
        
        return req -> {
            Boolean isPresent = employee.stream()
                    .map(employee1 -> employee1.getVendorId().equalsIgnoreCase(vendorId))
                    .findAny().orElse(false);
            
            if (isPresent) {
                employee.retainAll(employee.stream()
                        .filter(employee1 -> !employee1.getVendorId().equalsIgnoreCase(vendorId))
                        .collect(Collectors.toList()));
                return CompletableFuture.completedFuture("Successfully Deleted.");
            } else {
                return CompletableFuture.completedFuture("No Matching Vendor Id Was Found in the Database, Can't Delete.");
            }
        };
        
    }
    
    @Override
    public ServiceCall<Employee, String> updateEmployee(String vendorId) {
        
        return req -> {
            employee.add(req);
            int id = 0;
            try {
                id = OBJECT_MAPPER.readValue(req.getVendorId(), Integer.class);
            } catch (IOException ex) {
                System.out.println("can't transform ");
            }
            return cassandraSession.executeWrite("update user.userinfo Set age = ? where name = ? IF EXISTS",
                    id, req.getName())
                    .thenApply(notUsed -> "Successfully Updated.")
                    .exceptionally(throwable -> {
                        throw new RuntimeException();
                    });
        };
        
    }
}
