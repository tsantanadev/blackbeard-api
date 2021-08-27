package br.com.blackbeard.blackbeardapi.exceptions;

public class BarberShopImageException extends RuntimeException {

    public static final String IMAGE_DOES_NOT_BELONG_TO_BARBER_SHOP = "image does not belong to BarberShop";

    public BarberShopImageException(String msg) {
        super(msg);
    }

    public static BarberShopImageException imageDoesNotBelongToBarberShop() {
        return new BarberShopImageException(IMAGE_DOES_NOT_BELONG_TO_BARBER_SHOP);
    }
}
