package br.com.blackbeard.blackbeardapi.serviceTest;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.repositories.BarberShopRepository;
import br.com.blackbeard.blackbeardapi.service.BarberShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarberShopServiceTest {

    @InjectMocks
    private BarberShopService service;

    @Mock
    private BarberShopRepository repository;

    private BarberShop barberShop;

    @BeforeEach
    public void setup() {
        var address = Address.builder()
                .id(UUID.randomUUID())
                .build();

        this.barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .address(address).build();
    }

    @Test
    void shouldFindABarberShopById() {
        when(repository.findById(barberShop.getId())).thenReturn(Optional.of(barberShop));

        var result = service.findById(barberShop.getId());

        assertThat(result).isEqualTo(barberShop);
        verify(repository, times(1)).findById(barberShop.getId());
    }

    @Test
    void shouldThrowExceptionWhenFindABarberShopById() {
        when(repository.findById(barberShop.getId())).thenReturn(Optional.empty());

        var exception = assertThrows(ObjectNotFoundException.class,
        () -> service.findById(barberShop.getId()));

        assertThat(exception).hasMessage("Object not found");
    }
}
