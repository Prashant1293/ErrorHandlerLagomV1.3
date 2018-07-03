package edu.knoldus.com.hello.impl;

import akka.actor.ActorSystem;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import edu.knoldus.com.hello.api.Employee;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class HelloServiceImplTest {
    private final CassandraSession cassandraSession = new CassandraSession(ActorSystem.apply());
    
    @Test
    public void testCreationOfEmployeeObject() throws Exception {
        HelloServiceImpl helloService = new HelloServiceImpl(cassandraSession);
        Employee emp = Employee.builder().name("Prashant").vendorId("Symc7000").employmentType("Permanent").build();
        
        String succesMsg = helloService.createEmployee()
                .invoke(emp)
                .toCompletableFuture()
                .get(5, TimeUnit.SECONDS);
        
        assertEquals("Successfully Inserted.", succesMsg);
    }
    
    @Test
    public void testDeletionOfEmployeeObject() throws Exception {
        HelloServiceImpl helloService = new HelloServiceImpl(cassandraSession);
        
        String vendorId = "Symc7000";
        
        String succesMsg = helloService.deleteEmployee(vendorId)
                .invoke()
                .toCompletableFuture()
                .get(5, TimeUnit.SECONDS);
        
        assertEquals("No Matching Vendor Id Was Found in the Database, Can't Delete.", succesMsg);
    }
    
    @Test
    public void testFetchingOfEmployeeObject() throws Exception {
        HelloServiceImpl helloService = new HelloServiceImpl(cassandraSession);
        
        String vendorId = "Symc7000";
        
        Optional<Employee> employee = helloService.getEmployee(vendorId)
                .invoke()
                .toCompletableFuture()
                .get(5, TimeUnit.SECONDS);
        
        assertEquals(employee.isPresent(), false);
    }
    
}
