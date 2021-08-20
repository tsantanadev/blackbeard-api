package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.dtos.barbershop.BarberShopRequest;
import br.com.blackbeard.blackbeardapi.dtos.barbershop.BarberShopResponse;
import br.com.blackbeard.blackbeardapi.mappers.BarberShopMapper;
import br.com.blackbeard.blackbeardapi.service.BarberShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        var uri =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{Id}")
                        .buildAndExpand(barberShop.getId())
                        .toUri();

        return ResponseEntity.created(uri).body(BarberShopMapper.convertToResponse(barberShop));
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

    @PatchMapping("/logo")
    public ResponseEntity<Void> saveLogo(@RequestParam("barberShopId") UUID barberShopId,
                                         @RequestParam("logo") MultipartFile multipartFile) {
        var uri = service.saveLogo(barberShopId, multipartFile);
        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/image")
    public ResponseEntity<Void> saveImage(@RequestParam("barberShopId") UUID barberShopId,
                                         @RequestParam("image") MultipartFile multipartFile) {
        var uri = service.saveImages(barberShopId, multipartFile);
        return ResponseEntity.created(uri).build();
    }
}
