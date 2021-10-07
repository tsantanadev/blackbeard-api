package br.com.blackbeard.blackbeardapi.exceptions;

public class BarberArgumentException extends RuntimeException {

    public static final String BARBER_NAME_CANNOT_BE_REPEATED = "barber name cannot be repeated";

    public BarberArgumentException(String message) {
        super(message);
    }

    public static BarberArgumentException barberNameIsNotEquals() {
        return new BarberArgumentException(BARBER_NAME_CANNOT_BE_REPEATED);
    }
}
