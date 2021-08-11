package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Barber;
import br.com.blackbeard.blackbeardapi.repositories.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository repository;
    private final BarberShopService barberShopService;

    public Barber save(Barber barber, UUID idBarberShop) {
        barber.generateId();
        barber.setBarberShop(barberShopService.findById(idBarberShop));
        return repository.save(barber);
    }

    public Barber findById(UUID idBarber) {
        return repository.findById(idBarber)
                .orElseThrow(ObjectNotFoundException::new);
    }

    public void update(Barber barber, UUID idBarber) {
        var persistedBarber = findById(idBarber);
        persistedBarber.update(barber);
        repository.save(persistedBarber);
    }

    public Page<Barber> listAllBarberByIdBarberShop(UUID idBarberShop, Pageable pageable) {
        barberShopService.findById(idBarberShop);
        return repository.findAllByBarberShopId(idBarberShop, pageable);
    }
}
