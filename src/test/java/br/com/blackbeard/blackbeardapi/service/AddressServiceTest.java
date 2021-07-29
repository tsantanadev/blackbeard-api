package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.repositories.AddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    @Captor
    ArgumentCaptor<Address> addressCaptor;

    @Test
    void shouldUpdateAddress() {
        var persistedAddress = Address.builder()
                .id(UUID.randomUUID())
                .city("New york")
                .district("Brooklyn")
                .number("45")
                .build();

        var address = Address.builder()
                .id(UUID.randomUUID())
                .city("New jersey")
                .district("test")
                .street("Franklyn")
                .number("39")
                .build();

        var expected = Address.builder()
                .id(persistedAddress.getId())
                .city(address.getCity())
                .district(address.getDistrict())
                .street(address.getStreet())
                .number(address.getNumber())
                .build();

        addressService.update(persistedAddress, address);

        verify(addressRepository, times(1)).save(expected);
    }

    @Test
    void shouldSaveAddress() {
        var address = Address.builder()
                .id(null)
                .city("New jersey")
                .district("test")
                .street("Franklyn")
                .number("39")
                .build();

        addressService.save(address);

        verify(addressRepository).save(addressCaptor.capture());

        var result = addressCaptor.getValue();

        assertThat(result.getId()).isNotNull();
    }
}