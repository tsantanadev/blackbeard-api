package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.dtos.barber.BarberRequest;
import br.com.blackbeard.blackbeardapi.dtos.barber.BarberResponse;
import br.com.blackbeard.blackbeardapi.mappers.BarberMapper;
import br.com.blackbeard.blackbeardapi.service.BarberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/barber")
public class BarberController {

    @Autowired
    private BarberService service;

    @PostMapping
    public ResponseEntity<BarberResponse> insert(@RequestBody @Valid BarberRequest barberRequest,
                                                 @RequestParam("idBarberShop") UUID idBarberShop) {
        var barber = service.save(BarberMapper.convertToModel(barberRequest), idBarberShop);
        var uri =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{Id}")
                        .buildAndExpand(barber.getId())
                        .toUri();
        return ResponseEntity.created(uri).body(BarberMapper.convertToResponse(barber));
    }

    @GetMapping
    public Page<BarberResponse> listAll(@RequestParam("idBarberShop") UUID idBarberShop, Pageable pageable) {
        return service.listAllBarberByIdBarberShop(idBarberShop, pageable)
                .map(BarberMapper::convertToResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(BarberMapper.convertToResponse(service.findById(id)));
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid BarberRequest request,
                                       @RequestParam("idBarber") UUID idBarber) {
        service.update(BarberMapper.convertToModel(request), idBarber);
        return ResponseEntity.accepted().build();
    }
}
