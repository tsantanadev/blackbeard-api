package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.dtos.service.ServiceRequest;
import br.com.blackbeard.blackbeardapi.dtos.service.ServiceResponse;
import br.com.blackbeard.blackbeardapi.mappers.ServiceMapper;
import br.com.blackbeard.blackbeardapi.service.ServiceBarberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/barber/service")
@RequiredArgsConstructor
public class BarberServiceController {

    private final ServiceBarberService service;

    @PostMapping
    public ResponseEntity<ServiceResponse> insert(@RequestBody @Valid ServiceRequest serviceRequest,
                                                  @RequestParam("barberId") UUID barberId) {
        var serviceBarber = service.save(ServiceMapper.convertToModel(serviceRequest), barberId);

        var uri =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{Id}")
                        .buildAndExpand(serviceBarber.getId())
                        .toUri();
        return ResponseEntity.created(uri).body(ServiceMapper.convertToResponse(serviceBarber));
    }

    @GetMapping
    public Page<ServiceResponse> listAll(@RequestParam("barberId") UUID barberId, Pageable pageable) {
        return service.listAllServiceByIdBarber(barberId, pageable)
                .map(ServiceMapper::convertToResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ServiceMapper.convertToResponse(service.findById(id)));
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid ServiceRequest request,
                                       @RequestParam("serviceId") UUID serviceId) {
        service.update(ServiceMapper.convertToModel(request), serviceId);
        return ResponseEntity.accepted().build();
    }

}
