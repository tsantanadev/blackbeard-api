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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BarberShopServiceTest {

    @InjectMocks
    private BarberShopService service;

    @Mock
    private BarberShopRepository repository;

    private Address address;
    private BarberShop barberShop;

    @BeforeEach
    public void inicializar() {
        this.address = Address.builder().id(UUID.randomUUID()).build();
        this.barberShop = BarberShop.builder().id(UUID.randomUUID()).address(address).build();
    }

    @Test
    void shouldFindAnBarberShopById() {
        Mockito.when(repository.findById(barberShop.getId())).thenReturn(Optional.of(barberShop));
        var result = service.findById(barberShop.getId());

        assertThat(result.getId(), is(barberShop.getId()));
        assertEquals(result, barberShop);
    }

    @Test
    void shouldThrowExceptionWhenFindAnBarberShopById() {
        Mockito.when(repository.findById(barberShop.getId())).thenReturn(Optional.of(barberShop));
        service.findById(barberShop.getId());
        try {
            service.findById(UUID.randomUUID());
        } catch (ObjectNotFoundException e) {
            assertEquals("Object not found", e.getMessage());
        }
        assertThrows(ObjectNotFoundException.class, () -> service.findById(UUID.randomUUID()));
    }



}
