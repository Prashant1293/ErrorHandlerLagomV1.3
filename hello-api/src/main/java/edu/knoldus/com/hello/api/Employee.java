package edu.knoldus.com.hello.api;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class Employee {
    
    @NonNull
    String name;
    
    @NonNull
    String employmentType;
    
    @NonNull
    String vendorId;
    
}
