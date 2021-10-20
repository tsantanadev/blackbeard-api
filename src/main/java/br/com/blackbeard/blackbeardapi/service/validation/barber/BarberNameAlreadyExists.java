package br.com.blackbeard.blackbeardapi.service.validation.barber;

import br.com.blackbeard.blackbeardapi.exceptions.BarberArgumentException;
import br.com.blackbeard.blackbeardapi.models.Barber;
import br.com.blackbeard.blackbeardapi.repositories.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BarberNameAlreadyExists implements BarberValidation {

    private final BarberRepository repository;

    @Override
    public void validation(Barber barber) {

        var barberPersisted = repository.findBarberByBarberShopIdAndName(
                barber.getBarberShop().getId(),
                barber.getName());

        if (barberPersisted != null) {
            throw BarberArgumentException.barberNameAlreadyExists();
        }

    }
}
