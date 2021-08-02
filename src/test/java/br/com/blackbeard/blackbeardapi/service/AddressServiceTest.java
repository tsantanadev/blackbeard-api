package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.repositories.AddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private BarberShopService barberShopService;

    @Captor
    ArgumentCaptor<Address> addressCaptor;

    @Test
    void shouldUpdateAddress() {
        var persistedAddressId = UUID.randomUUID();
        var persistedAddress = Address.builder()
                .id(persistedAddressId)
                .city("New york")
                .district("Brooklyn")
                .number("45")
                .build();

        var address = Address.builder()
                .city("New jersey")
                .district("test")
                .street("Franklyn")
                .number("39")
                .build();

        var expected = Address.builder()
                .id(persistedAddressId)
                .city(address.getCity())
                .district(address.getDistrict())
                .street(address.getStreet())
                .number(address.getNumber())
                .build();

        when(addressRepository.findById(persistedAddressId)).thenReturn(Optional.of(persistedAddress));

        addressService.update(address, persistedAddressId);

        verify(addressRepository, times(1)).save(expected);
    }

    @Test
    void shouldThrownAnExceptionWhenUpdateWithAnInvalidId() {
        var invalidId = UUID.randomUUID();

        var address = Address.builder()
                .city("New jersey")
                .district("test")
                .street("Franklyn")
                .number("39")
                .build();

        when(addressRepository.findById(invalidId)).thenReturn(Optional.empty());

        var exception = assertThrows(ObjectNotFoundException.class,
                () -> addressService.update(address, invalidId));

        assertThat(exception).hasMessage("Object not found");
    }

    @Test
    void shouldSaveAddress() {
        var barberShopId = UUID.randomUUID();
        var address = Address.builder()
                .id(null)
                .city("New jersey")
                .district("test")
                .street("Franklyn")
                .number("39")
                .build();

        addressService.save(address, barberShopId);

        verify(addressRepository).save(addressCaptor.capture());

        var result = addressCaptor.getValue();

        verify(barberShopService, times(1)).saveAddress(result, barberShopId);
        assertThat(result.getId()).isNotNull();
    }
}