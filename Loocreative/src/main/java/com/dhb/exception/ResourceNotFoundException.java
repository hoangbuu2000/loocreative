package com.dhb.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String field;
    private String value;

    public ResourceNotFoundException(String resourceName, String field, String value) {
        super(String.format("%s not found width %s: %s", resourceName, field, value));
        this.resourceName = resourceName;
        this.field = field;
        this.value = value;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
