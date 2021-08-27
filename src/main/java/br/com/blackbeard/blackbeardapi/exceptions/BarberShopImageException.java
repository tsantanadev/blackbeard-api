package br.com.blackbeard.blackbeardapi.exceptions;

public class BarberShopImageException extends RuntimeException {

    public static final String LIMIT_OF_IMAGES_EXCEEDED = "image does not belong to BarberShop";

    public BarberShopImageException(String msg) {
        super(msg);
    }

    public static BarberShopImageException imageDoesNotBelongToBarberShop() {
        return new BarberShopImageException(LIMIT_OF_IMAGES_EXCEEDED);
    }
}
