package br.com.blackbeard.blackbeardapi.service.validation.barber;

import br.com.blackbeard.blackbeardapi.exceptions.BarberArgumentException;
import br.com.blackbeard.blackbeardapi.models.Barber;
import br.com.blackbeard.blackbeardapi.repositories.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BarberNameIsNotEquals implements ValidationBarber {

    private final BarberRepository repository;

    @Override
    public void validation(Barber barber) {
        var listBarber = repository.findAllByBarberShopId(barber.getBarberShop().getId());
        listBarber.forEach(obj -> {
            if (obj.getName().equals(barber.getName())) {
                throw BarberArgumentException.barberNameIsNotEquals();
            }
        });

    }
}
