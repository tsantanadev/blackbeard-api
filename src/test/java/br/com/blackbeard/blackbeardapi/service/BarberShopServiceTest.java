package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.repositories.BarberShopRepository;
import br.com.blackbeard.blackbeardapi.service.AddressService;
import br.com.blackbeard.blackbeardapi.service.BarberShopService;
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

    @Mock
    private AddressService addressService;

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
        var newAddress = Address.builder().id(null).build();
        var newBarberShop = BarberShop.builder()
                .id(null)
                .address(newAddress)
                .build();

        when(addressService.save(newAddress))
                .thenReturn(Address.builder().id(UUID.randomUUID()).build());

        service.save(newBarberShop);

        verify(repository).save(barberShopCaptor.capture());
        var result = barberShopCaptor.getValue();

        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress().getId()).isNotNull();
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
        var persisteBarberShop = BarberShop.builder()
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
                .id(persisteBarberShop.getId())
                .name(barberShop.getName())
                .imageUrl(barberShop.getImageUrl())
                .address(persisteBarberShop.getAddress())
                .build();

        when(repository.findById(persisteBarberShop.getId())).thenReturn(Optional.of(persisteBarberShop));

        service.update(barberShop, persisteBarberShop.getId());

        verify(repository, times(1)).save(expected);

    }
}