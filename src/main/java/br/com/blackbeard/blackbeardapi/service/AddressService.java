package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    public void update(Address persistedAddress, Address address) {
        persistedAddress.update(address);
        repository.save(persistedAddress);
    }
}
