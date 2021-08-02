package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.repositories.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository repository;

    private final BarberShopService barberShopService;

    public void update(Address address, UUID addressId) {
        var persistedAddress = findById(addressId);
        persistedAddress.update(address);

        repository.save(persistedAddress);
    }

    private Address findById(UUID addressId) {
        return repository.findById(addressId).orElseThrow(ObjectNotFoundException::new);
    }

    @Transactional
    public Address save(Address address, UUID barberShopId) {
        address.generateId();
        final var persistedAddress = repository.save(address);

        barberShopService.saveAddress(address, barberShopId);
        return persistedAddress;
    }
}
