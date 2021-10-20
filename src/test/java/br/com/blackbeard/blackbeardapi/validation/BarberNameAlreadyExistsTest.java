package br.com.blackbeard.blackbeardapi.validation;

import br.com.blackbeard.blackbeardapi.exceptions.BarberArgumentException;
import br.com.blackbeard.blackbeardapi.models.Barber;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.repositories.BarberRepository;
import br.com.blackbeard.blackbeardapi.service.validation.barber.BarberNameAlreadyExists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarberNameAlreadyExistsTest {

    @InjectMocks
    private BarberNameAlreadyExists barberNameAlreadyExists;

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

        when(repository.findBarberByBarberShopIdAndName(
                barber.getBarberShop()
                        .getId(), barber.getName())).thenReturn(null);

        barberNameAlreadyExists.validation(barber);

        verify(repository, times(1))
                .findBarberByBarberShopIdAndName(barber.getBarberShop().getId(),
                        barber.getName());
    }

    @Test
    void shouldThrowException() {
        var barber = Barber.builder()
                .name("test")
                .barberShop(BarberShop.builder()
                        .id(UUID.randomUUID())
                        .build())
                .build();

        when(repository.findBarberByBarberShopIdAndName(
                barber.getBarberShop()
                        .getId(), barber.getName())).thenReturn(barber);

        var exception = assertThrows(BarberArgumentException.class,
                () -> barberNameAlreadyExists.validation(barber));

        assertThat(exception).hasMessage("There is already a barber with that name for this barbershop");
    }
}
