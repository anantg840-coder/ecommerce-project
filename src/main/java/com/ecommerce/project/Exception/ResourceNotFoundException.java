package com.ecommerce.project.Exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {
    String resourcename;
    String feild;
    String fieldName;
    Long fiendId;

    public ResourceNotFoundException(HttpStatus notFound, String resourceNotFound) {
    }
    public ResourceNotFoundException(String resourcename, String field, String fieldName) {
        super(String.format("%s not found with %s: %s", resourcename,field,fieldName));
        this.resourcename = resourcename;
        this.feild = feild;
        this.fieldName = fieldName;
    }



    public ResourceNotFoundException(String resourcename, String feild, long fiendId) {
        super(String.format("%s not found with %s: %d" , resourcename,feild,fiendId));
        this.resourcename = resourcename;
        this.feild = feild;
        this.fiendId = fiendId;
    }
}
