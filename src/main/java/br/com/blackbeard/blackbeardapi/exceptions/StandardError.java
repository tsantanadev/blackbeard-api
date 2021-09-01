package br.com.blackbeard.blackbeardapi.exceptions;

import lombok.Data;

@Data
public class StandardError<T> {

    private Integer status;
    private String message;
    private T error;
    private long timeStamp;
    private String path;

    public StandardError(Integer status, String message, T error, long timeStamp, String path) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.timeStamp = timeStamp;
        this.path = path;
    }
}
