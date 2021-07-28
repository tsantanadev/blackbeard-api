package br.com.blackbeard.blackbeardapi.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    private static final String OBJECT_NOT_FOUND = "Object not found";

    public ObjectNotFoundException() {
        super(OBJECT_NOT_FOUND);
    }
}
