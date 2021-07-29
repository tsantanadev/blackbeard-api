package br.com.blackbeard.blackbeardapi.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AddressTest {

    @Test
    void shouldUpdate() {
        var address1 = Address.builder()
                .id(UUID.randomUUID())
                .city("New york")
                .district("Brooklyn")
                .number("45")
                .build();

        var address2 = Address.builder()
                .id(UUID.randomUUID())
                .city("New jersey")
                .district("test")
                .street("Franklyn")
                .number("39")
                .build();

        var expected = Address.builder()
                .id(address1.getId())
                .city(address2.getCity())
                .district(address2.getDistrict())
                .street(address2.getStreet())
                .number(address2.getNumber())
                .build();

        address1.update(address2);

        assertThat(address1).isEqualTo(expected);
    }

    @Test
    void shouldGenerateId() {
        var address = Address.builder().build();

        address.generateId();

        assertThat(address.getId()).isNotNull();
    }
}