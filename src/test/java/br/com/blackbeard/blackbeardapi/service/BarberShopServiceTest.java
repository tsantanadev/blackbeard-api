package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectAlreadyCreatedException;
import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.repositories.BarberShopRepository;
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

    @Captor
    ArgumentCaptor<BarberShop> barberShopCaptor;

    private BarberShop barberShop;

    @BeforeEach
    void setup() {
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
        var barberShopId = barberShop.getId();

        when(repository.findById(barberShopId)).thenReturn(Optional.empty());

        var exception = assertThrows(ObjectNotFoundException.class,
                () -> service.findById(barberShopId));

        assertThat(exception).hasMessage("Object not found");
    }

    @Test
    void shouldCreateAnBarberShop() {
        var newBarberShop = BarberShop.builder()
                .id(null)
                .build();

        service.save(newBarberShop);

        verify(repository).save(barberShopCaptor.capture());
        var result = barberShopCaptor.getValue();

        assertThat(result.getId()).isNotNull();
    }

    @Test
    void shouldFindAllBarberShop() {
        final var pageable = PageRequest.of(20, 20);

        when(repository.findAll(pageable)).thenReturn(Page.empty());

        service.listAll(pageable);

        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void shouldUpdateAnBarberShopById() {
        var persistedBarberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .name("teste")
                .imageUrl("http://www.teste.com")
                .address(Address.builder().build())
                .build();

        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .name("teste 2")
                .imageUrl("http://www.teste2.com")
                .address(Address.builder().build())
                .build();

        var expected = BarberShop.builder()
                .id(persistedBarberShop.getId())
                .name(barberShop.getName())
                .imageUrl(barberShop.getImageUrl())
                .address(persistedBarberShop.getAddress())
                .build();

        when(repository.findById(persistedBarberShop.getId())).thenReturn(Optional.of(persistedBarberShop));

        service.update(barberShop, persistedBarberShop.getId());

        verify(repository, times(1)).save(expected);
    }

    @Test
    void shouldSaveAddress() {
        var address = Address.builder()
                .id(UUID.randomUUID())
                .city("New jersey")
                .district("test")
                .street("Franklyn")
                .number("39")
                .build();

        var persistedBarberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .name("test")
                .imageUrl("http://www.teste.com")
                .build();

        var expected = BarberShop.builder()
                .id(persistedBarberShop.getId())
                .name(persistedBarberShop.getName())
                .imageUrl(persistedBarberShop.getImageUrl())
                .address(address)
                .build();

        when(repository.findById(persistedBarberShop.getId())).thenReturn(Optional.of(persistedBarberShop));

        service.saveAddress(address, persistedBarberShop.getId());

        verify(repository).save(barberShopCaptor.capture());

        assertThat(barberShopCaptor.getValue()).isEqualTo(expected);
    }

    @Test
    void shouldThrowAnExceptionWhenBarberShopAlreadyHasAddress() {
        var address = Address.builder()
                .id(UUID.randomUUID())
                .city("New jersey")
                .district("test")
                .street("Franklyn")
                .number("39")
                .build();

        var persistedBarberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .address(address)
                .name("test")
                .imageUrl("http://www.teste.com")
                .build();

        when(repository.findById(persistedBarberShop.getId())).thenReturn(Optional.of(persistedBarberShop));

        final var exception = assertThrows(ObjectAlreadyCreatedException.class,
                () -> service.saveAddress(address, persistedBarberShop.getId()));

        assertThat(exception.getMessage()).isEqualTo("Address already created. Try update it");
    }
}
