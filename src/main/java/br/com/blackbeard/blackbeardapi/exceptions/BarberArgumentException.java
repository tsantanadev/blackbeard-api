package br.com.blackbeard.blackbeardapi.exceptions;

public class BarberArgumentException extends RuntimeException {

    public static final String BARBER_NAME_CANNOT_BE_REPEATED = "There is already a barber with that name for this barbershop";

    private BarberArgumentException(String message) {
        super(message);
    }

    public static BarberArgumentException barberNameAlreadyExists() {
        return new BarberArgumentException(BARBER_NAME_CANNOT_BE_REPEATED);
    }
}
