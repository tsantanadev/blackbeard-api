package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectAlreadyCreatedException;
import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.repositories.BarberShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
public class BarberShopService {

    private final BarberShopRepository repository;

    @Autowired
    public BarberShopService(BarberShopRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BarberShop save(BarberShop barberShop) {
        barberShop.generateId();
        return repository.save(barberShop);
    }

    @Transactional
    public void update(BarberShop barberShop, UUID barberShopId) {
        var persistedBarberShop = findById(barberShopId);
        persistedBarberShop.update(barberShop);

        repository.save(persistedBarberShop);
    }

    public Page<BarberShop> listAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public BarberShop findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(ObjectNotFoundException::new);
    }

    public void saveAddress(Address address, UUID barberShopId) {
        var barberShop = findById(barberShopId);

        if (nonNull(barberShop.getAddress())) {
            throw new ObjectAlreadyCreatedException("Address already created. Try update it");
        }

        barberShop.setAddress(address);
        repository.save(barberShop);
    }
}
