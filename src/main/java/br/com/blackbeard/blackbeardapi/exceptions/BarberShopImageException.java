package br.com.blackbeard.blackbeardapi.exceptions;

public class BarberShopImageException extends RuntimeException {

    public static final String LIMIT_OF_IMAGES_EXCEEDED = "image does not belong to this barbershop";

    public BarberShopImageException() {
        super(LIMIT_OF_IMAGES_EXCEEDED);
    }
}
