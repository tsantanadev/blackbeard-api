package br.com.blackbeard.blackbeardapi.exceptions;

public class BarberShopImageLimitException extends RuntimeException {

    public static final String LIMIT_OF_IMAGES_EXCEEDED = "limit of images exceeded";

    public BarberShopImageLimitException() {
        super(LIMIT_OF_IMAGES_EXCEEDED);
    }
}
