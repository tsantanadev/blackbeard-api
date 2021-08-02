package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.dtos.address.AddressRequest;
import br.com.blackbeard.blackbeardapi.mappers.AddressMapper;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("barberShop/address")
public class AddressController {

    @Autowired
    private AddressService service;

    @PostMapping
    public ResponseEntity<Address> create(@RequestBody @Valid AddressRequest addressRequest, @RequestParam UUID barberShopId) {
        var addressToInsert = AddressMapper.convertToModel(addressRequest);

        final var address = service.save(addressToInsert, barberShopId);

        var uri =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{Id}")
                        .buildAndExpand(address.getId())
                        .toUri();

        return ResponseEntity.created(uri).body(address);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AddressRequest addressRequest, @RequestParam UUID addressId) {
        var addressToUpdate = AddressMapper.convertToModel(addressRequest);

        service.update(addressToUpdate, addressId);

        return ResponseEntity.accepted().build();
    }
}
