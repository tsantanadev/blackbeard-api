package br.com.blackbeard.blackbeardapi.validation;

import br.com.blackbeard.blackbeardapi.exceptions.BarberArgumentException;
import br.com.blackbeard.blackbeardapi.models.Barber;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.repositories.BarberRepository;
import br.com.blackbeard.blackbeardapi.service.validation.barber.BarberNameIsNotEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarberNameIsNotEqualsTest {

    @InjectMocks
    private BarberNameIsNotEquals barberNameIsNotEquals;

    @Mock
    private BarberRepository repository;

    @Test
    void shouldCall() {
        var barber = Barber.builder()
                .name("test")
                .barberShop(BarberShop.builder()
                        .id(UUID.randomUUID())
                        .build())
                .build();

        var barber2 = Barber.builder()
                .name("test 2")
                .barberShop(BarberShop.builder()
                        .id(UUID.randomUUID())
                        .build())
                .build();

        var listBarber = new ArrayList<Barber>();
        listBarber.add(barber2);

        when(repository.findAllByBarberShopId(
                barber.getBarberShop()
                        .getId())).thenReturn(listBarber);

        barberNameIsNotEquals.validation(barber);

        verify(repository, times(1))
                .findAllByBarberShopId(barber.getBarberShop().getId());

    }

    @Test
    void shouldThrowException() {
        var barber = Barber.builder()
                .name("test")
                .barberShop(BarberShop.builder()
                        .id(UUID.randomUUID())
                        .build())
                .build();

        var listBarber = new ArrayList<Barber>();
        listBarber.add(barber);

        when(repository.findAllByBarberShopId(
                barber.getBarberShop()
                        .getId())).thenReturn(listBarber);

        var exception = assertThrows(BarberArgumentException.class,
                () -> barberNameIsNotEquals.validation(barber));

        assertThat(exception).hasMessage("barber name cannot be repeated");
    }
}
