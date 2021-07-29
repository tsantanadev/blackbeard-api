package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.repositories.BarberShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class BarberShopService {

    private final BarberShopRepository repository;
    private final AddressService addressService;

    @Autowired
    public BarberShopService(BarberShopRepository repository, AddressService addressService) {
        this.repository = repository;
        this.addressService = addressService;
    }

    public BarberShop create(BarberShop barberShop) {
        barberShop.generateId();
        addressService.save(barberShop.getAddress());
        return repository.save(barberShop);
    }

    @Transactional
    public void update(BarberShop barberShop, UUID barberShopId) {
        var persistedBarberShop = findById(barberShopId);
        persistedBarberShop.update(barberShop);

        addressService.update(persistedBarberShop.getAddress(), barberShop.getAddress());
        repository.save(persistedBarberShop);
    }

    public Page<BarberShop> listAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public BarberShop findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(ObjectNotFoundException::new);
    }
}
