package br.com.blackbeard.blackbeardapi.exceptions;

import lombok.Data;

@Data
public class StandardError<T> {

    private Integer status;
    private T message;
    private String error;
    private long timeStamp;
    private String path;

    public StandardError(Integer status, T message, String error, long timeStamp, String path) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.timeStamp = timeStamp;
        this.path = path;
    }
}
