package br.com.blackbeard.blackbeardapi.exceptions;

import lombok.Data;

import java.io.Serializable;

@Data
public class StandardException<T> implements Serializable {

    private Integer status;
    private T message;
    private String error;
    private long timeStamp;
    private String path;

    public StandardException(Integer status, T message, String error, long timeStamp, String path) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.timeStamp = timeStamp;
        this.path = path;
    }
}
