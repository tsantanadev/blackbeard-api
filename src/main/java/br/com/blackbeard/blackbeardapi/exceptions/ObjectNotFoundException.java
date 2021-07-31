package br.com.blackbeard.blackbeardapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException {

    private static final String OBJECT_NOT_FOUND = "Object not found";

    public ObjectNotFoundException() {
        super(OBJECT_NOT_FOUND);
    }
}
