package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.BarberServiceModel;
import br.com.blackbeard.blackbeardapi.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ServiceBarberService {

    private final ServiceRepository repository;
    private final br.com.blackbeard.blackbeardapi.service.BarberService barberService;

    public BarberServiceModel save(BarberServiceModel service, UUID idBarber) {
        service.generateId();
        service.setBarber(barberService.findById(idBarber));
        return repository.save(service);
    }

    public BarberServiceModel findById(UUID idService) {
        return repository.findById(idService)
                .orElseThrow(ObjectNotFoundException::new);
    }

    public Page<BarberServiceModel> listAllServiceByIdBarber(UUID idBarber, Pageable pageable) {
        barberService.findById(idBarber);
        return repository.findAllByBarberId(idBarber, pageable);
    }

    public void update(BarberServiceModel service, UUID idService) {
        var persistedService = findById(idService);
        persistedService.update(service);
        repository.save(persistedService);
    }
}
