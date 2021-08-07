package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.dtos.barber.BarberScheduleRequest;
import br.com.blackbeard.blackbeardapi.mappers.BarberScheduleMapper;
import br.com.blackbeard.blackbeardapi.models.BarberSchedule;
import br.com.blackbeard.blackbeardapi.service.BarberScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/barber/schedule")
class BarberScheduleController {

    @Autowired
    private BarberScheduleService service;

    @PostMapping
    public ResponseEntity<BarberSchedule> insert(@RequestBody @Valid BarberScheduleRequest scheduleRequest,
                                                 @RequestParam UUID barberId) {
        var schedule = service.save(BarberScheduleMapper.convertToModel(scheduleRequest), barberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
    }

//    @GetMapping
//    public Page<BarberResponse> listAll(@RequestParam("idBarberShop") UUID idBarberShop, Pageable pageable) {
//        return service.listAllBarberByIdBarberShop(idBarberShop, pageable)
//                .map(BarberMapper::convertToResponse);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<BarberResponse> findById(@PathVariable UUID id) {
//        return ResponseEntity.ok(BarberMapper.convertToResponse(service.findById(id)));
//    }
//
//    @PutMapping
//    public ResponseEntity<Void> update(@RequestBody @Valid BarberRequest request,
//                                       @RequestParam("idBarber") UUID idBarber) {
//        service.update(BarberMapper.convertToModel(request), idBarber);
//        return ResponseEntity.accepted().build();
//    }
}
