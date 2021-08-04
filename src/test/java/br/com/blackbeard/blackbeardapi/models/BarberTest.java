package br.com.blackbeard.blackbeardapi.models;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BarberTest {

    @Test
    void shouldUpdate() {
        var persistedBarber = Barber.builder()
                .id(UUID.randomUUID())
                .name("teste")
                .barberShop(BarberShop.builder().build())
                .build();

        var barber = Barber.builder()
                .id(UUID.randomUUID())
                .name("teste 2")
                .build();

        var excepted = Barber.builder()
                .id(persistedBarber.getId())
                .name(barber.getName())
                .barberShop(persistedBarber.getBarberShop())
                .build();

        persistedBarber.update(barber);

        assertThat(persistedBarber).isEqualTo(excepted);
    }

    @Test
    void shouldGenerateId() {
        var barber = Barber.builder().build();

        barber.generateId();

        assertThat(barber.getId()).isNotNull();
    }
}
