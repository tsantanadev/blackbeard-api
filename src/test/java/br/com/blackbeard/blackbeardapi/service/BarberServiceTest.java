package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.models.Barber;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.repositories.BarberRepository;
import br.com.blackbeard.blackbeardapi.service.validation.barber.BarberValidation;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarberServiceTest {

    @InjectMocks
    private BarberService service;

    @Mock
    private BarberRepository repository;

    @Mock
    private BarberShopService barberShopService;

    @Mock
    private List<BarberValidation> barberValidation;

    @Captor
    ArgumentCaptor<Barber> barberCaptor;

    private Barber barber;

    @BeforeEach
    void setup() {
        var address = Address.builder()
                .id(UUID.randomUUID())
                .build();

        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .address(address)
                .build();

        this.barber = Barber.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();
    }

    @Test
    void shouldFindAnBarberById() {
        when(repository.findById(barber.getId())).thenReturn(Optional.of(barber));

        var result = service.findById(barber.getId());

        assertThat(result).isEqualTo(barber);
        verify(repository, times(1)).findById(barber.getId());
    }

    @Test
    void shouldCreateAnBarber() {
        var newBarber = Barber.builder()
                .id(null)
                .barberShop(barber.getBarberShop())
                .build();

        when(barberShopService.findById(barber.getBarberShop().getId())).thenReturn(barber.getBarberShop());

        service.save(newBarber, barber.getBarberShop().getId());

        verify(repository).save(barberCaptor.capture());

        var result = barberCaptor.getValue();

        assertThat(result.getId()).isNotNull();
        assertThat(result.getBarberShop()).isEqualTo(barber.getBarberShop());
    }

    @Test
    void shouldThrowExceptionWhenFindABarberById() {
        var barberId = UUID.randomUUID();
        when(repository.findById(barberId)).thenReturn(Optional.empty());

        var exception = assertThrows(ObjectNotFoundException.class,
                () -> service.findById(barberId));

        assertThat(exception).hasMessage("Object not found");
    }

    @Test
    void shouldAllBarberByIdBarberShop() {
        final var pageable = PageRequest.of(20, 20);

        when(repository.findAllByBarberShopId(barber.getBarberShop().getId(), pageable)).thenReturn(Page.empty());

        service.listAllBarberByIdBarberShop(barber.getBarberShop().getId(), pageable);

        verify(repository, times(1))
                .findAllByBarberShopId(barber.getBarberShop().getId(), pageable);
    }

    @Test
    void shouldUpdateAnBarberById() {
        var persistedBarber = Barber.builder()
                .id(UUID.randomUUID())
                .name("test")
                .barberShop(barber.getBarberShop())
                .build();

        var barber = Barber.builder()
                .id(UUID.randomUUID())
                .name("test 2")
                .build();

        var excepted = Barber.builder()
                .id(persistedBarber.getId())
                .name(barber.getName())
                .barberShop(persistedBarber.getBarberShop())
                .build();

        when(repository.findById(persistedBarber.getId())).thenReturn(Optional.of(persistedBarber));

        service.update(barber, persistedBarber.getId());

        verify(repository, times(1)).save(excepted);
    }

    @Test
    void shouldCallValidationWhenSaveABarber() {

        service.save(barber, barber.getBarberShop().getId());

        barberValidation.forEach(validation -> {
            verify(validation, times(1)).validate(barber);
        });
    }
}
