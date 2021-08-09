package br.com.blackbeard.blackbeardapi.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceBarberTest {

    @Test
    void shouldUpdate() {
        var persistedService = ServiceBarber.builder()
                .id(UUID.randomUUID())
                .name("teste")
                .description("teste teste teste teste teste")
                .price(BigDecimal.valueOf(15.5))
                .duration(BigDecimal.valueOf(10))
                .barber(Barber.builder().build())
                .build();

        var serviceBarberUpdate = ServiceBarber.builder()
                .id(UUID.randomUUID())
                .name("teste 2")
                .description("teste teste teste teste teste 2")
                .price(BigDecimal.valueOf(5.5))
                .duration(BigDecimal.valueOf(5))
                .build();

        var excepted = ServiceBarber.builder()
                .id(persistedService.getId())
                .name(serviceBarberUpdate.getName())
                .description(serviceBarberUpdate.getDescription())
                .duration(serviceBarberUpdate.getDuration())
                .price(serviceBarberUpdate.getPrice())
                .barber(persistedService.getBarber())
                .build();

        persistedService.update(serviceBarberUpdate);

        assertThat(persistedService).isEqualTo(excepted);
    }

    @Test
    void shouldGenerateId() {
        var service = ServiceBarber.builder().build();

        service.generateId();

        assertThat(service.getId()).isNotNull();
    }
}
