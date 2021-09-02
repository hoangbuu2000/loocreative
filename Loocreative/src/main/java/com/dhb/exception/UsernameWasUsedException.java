package com.dhb.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameWasUsedException extends RuntimeException{
    private String username;

    public UsernameWasUsedException(String username) {
        super(String.format("This username: %s was used.", username));
        this.username = username;
    }
}
