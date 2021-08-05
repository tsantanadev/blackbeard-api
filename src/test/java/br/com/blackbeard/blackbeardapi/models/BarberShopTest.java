package br.com.blackbeard.blackbeardapi.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BarberShopTest {

    @Test
    void shouldUpdate() {
        var barberShop1 = BarberShop.builder()
                .id(UUID.randomUUID())
                .name("teste")
                .address(Address.builder().build())
                .build();

        var barberShop2 = BarberShop.builder()
                .id(UUID.randomUUID())
                .name("teste 2")
                .address(Address.builder().build())
                .build();

        var expected = BarberShop.builder()
                .id(barberShop1.getId())
                .name(barberShop2.getName())
                .address(barberShop1.getAddress())
                .build();

        barberShop1.update(barberShop2);

        assertThat(barberShop1).isEqualTo(expected);
    }

    @Test
    void shouldGenerateId() {
        var barberShop = BarberShop.builder().build();

        barberShop.generateId();

        assertThat(barberShop.getId()).isNotNull();
    }
}