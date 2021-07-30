package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.dtos.barberShop.BarberShopRequest;
import br.com.blackbeard.blackbeardapi.dtos.barberShop.BarberShopResponse;
import br.com.blackbeard.blackbeardapi.mappers.BarberShopMapper;
import br.com.blackbeard.blackbeardapi.service.BarberShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/barberShop")
public class BarberShopController {

    @Autowired
    private BarberShopService service;

    @PostMapping
    public ResponseEntity<BarberShopResponse> insert(@RequestBody @Valid BarberShopRequest request) {
        var barberShop = service.save(BarberShopMapper.convertToModel(request));
        return ResponseEntity.ok(BarberShopMapper.convertToResponse(barberShop));
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid BarberShopRequest request,
                                       @RequestParam("barberShopId") UUID barberShopId) {
        service.update(BarberShopMapper.convertToModel(request), barberShopId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public Page<BarberShopResponse> listAll(Pageable pageable) {
        return service.listAll(pageable).map(BarberShopMapper::convertToResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberShopResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(BarberShopMapper.convertToResponse(service.findById(id)));
    }
}
