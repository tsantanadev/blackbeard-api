package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Barber;
import br.com.blackbeard.blackbeardapi.models.ServiceBarber;
import br.com.blackbeard.blackbeardapi.repositories.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceBarberServiceTest {

    @InjectMocks
    private ServiceBarberService service;

    @Mock
    private ServiceRepository repository;

    @Mock
    private BarberService barberService;

    @Captor
    ArgumentCaptor<ServiceBarber> serviceCaptor;

    private ServiceBarber serviceBarber;

    @BeforeEach
    void setup() {
        var barber = Barber.builder()
                .id(UUID.randomUUID())
                .build();

        this.serviceBarber = ServiceBarber.builder()
                .id(UUID.randomUUID())
                .barber(barber)
                .build();
    }

    @Test
    void shouldFindAnServiceById() {
        when(repository.findById(serviceBarber.getId())).thenReturn(Optional.of(serviceBarber));

        var result = service.findById(serviceBarber.getId());

        assertThat(result).isEqualTo(serviceBarber);
        verify(repository, times(1)).findById(serviceBarber.getId());
    }

    @Test
    void shouldCreateAnService() {
        var newService = ServiceBarber.builder()
                .id(null)
                .barber(serviceBarber.getBarber())
                .build();


        when(barberService.findById(serviceBarber.getBarber().getId())).thenReturn(serviceBarber.getBarber());

        service.save(newService, serviceBarber.getBarber().getId());

        verify(repository).save(serviceCaptor.capture());

        var result = serviceCaptor.getValue();

        assertThat(result.getId()).isNotNull();
        assertThat(result.getBarber()).isEqualTo(serviceBarber.getBarber());
    }

    @Test
    void shouldThrowExceptionWhenFindAServiceById() {
        var serviceId = UUID.randomUUID();
        when(repository.findById(serviceId)).thenReturn(Optional.empty());

        var exception = assertThrows(ObjectNotFoundException.class,
                () -> service.findById(serviceId));

        assertThat(exception).hasMessage("Object not found");
    }

    @Test
    void shouldAllServiceByIdBarber() {
        final var pageable = PageRequest.of(20, 20);

        when(repository.findAllByBarberId(serviceBarber.getBarber().getId(), pageable)).thenReturn(Page.empty());

        service.listAllServiceByIdBarber(serviceBarber.getBarber().getId(), pageable);

        verify(repository, times(1))
                .findAllByBarberId(serviceBarber.getBarber().getId(), pageable);
    }

    @Test
    void shouldUpdateAnServiceById() {
        var persistedService = ServiceBarber.builder()
                .id(UUID.randomUUID())
                .name("teste")
                .description("teste teste teste teste teste")
                .price(BigDecimal.valueOf(15.5))
                .duration(BigDecimal.valueOf(10))
                .barber(serviceBarber.getBarber())
                .build();

        var serviceBarberUpdate = ServiceBarber.builder()
                .name("teste 2")
                .description("teste teste teste teste teste 2")
                .price(BigDecimal.valueOf(5.5))
                .duration(BigDecimal.valueOf(5))
                .barber(serviceBarber.getBarber())
                .build();

        var excepted = ServiceBarber.builder()
                .id(persistedService.getId())
                .name(serviceBarberUpdate.getName())
                .description(serviceBarberUpdate.getDescription())
                .duration(serviceBarberUpdate.getDuration())
                .price(serviceBarberUpdate.getPrice())
                .barber(persistedService.getBarber())
                .build();

        when(repository.findById(persistedService.getId())).thenReturn(Optional.of(persistedService));

        service.update(serviceBarberUpdate, persistedService.getId());

        verify(repository, times(1)).save(excepted);
    }
}
