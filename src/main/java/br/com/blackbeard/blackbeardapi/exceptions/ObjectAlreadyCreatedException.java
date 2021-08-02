package br.com.blackbeard.blackbeardapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ObjectAlreadyCreatedException extends RuntimeException {

    public ObjectAlreadyCreatedException(String msg) {
        super(msg);
    }
}
