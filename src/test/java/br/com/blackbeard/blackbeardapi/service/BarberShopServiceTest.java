package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.ObjectAlreadyCreatedException;
import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.repositories.BarberShopRepository;
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
import org.springframework.mock.web.MockMultipartFile;

import java.net.URI;
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
    private S3Service s3Service;

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
        var newBarberShop = BarberShop.builder()
                .id(null)
                .build();

        when(repository.save(any())).thenReturn(BarberShop.builder()
                .id(UUID.randomUUID())
                .build());

        service.save(newBarberShop);

        assertThat(newBarberShop.getId()).isNotNull();
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
        var persistedBarberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .name("teste")
                .address(Address.builder().build())
                .build();

        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .name("teste 2")
                .address(Address.builder().build())
                .build();

        var expected = BarberShop.builder()
                .id(persistedBarberShop.getId())
                .name(barberShop.getName())
                .address(persistedBarberShop.getAddress())
                .build();

        when(repository.findById(persistedBarberShop.getId())).thenReturn(Optional.of(persistedBarberShop));

        service.update(barberShop, persistedBarberShop.getId());

        verify(repository, times(1)).save(expected);
    }

    @Test
    void shouldSaveAddress() {
        var address = Address.builder()
                .id(UUID.randomUUID())
                .city("New jersey")
                .district("test")
                .street("Franklyn")
                .number("39")
                .build();

        var persistedBarberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .name("test")
                .build();

        var expected = BarberShop.builder()
                .id(persistedBarberShop.getId())
                .name(persistedBarberShop.getName())
                .address(address)
                .build();

        when(repository.findById(persistedBarberShop.getId())).thenReturn(Optional.of(persistedBarberShop));

        service.saveAddress(address, persistedBarberShop.getId());

        verify(repository).save(barberShopCaptor.capture());

        assertThat(barberShopCaptor.getValue()).isEqualTo(expected);
    }

    @Test
    void shouldThrowAnExceptionWhenBarberShopAlreadyHasAddress() {
        var barberShopId = UUID.randomUUID();

        var address = Address.builder()
                .id(UUID.randomUUID())
                .city("New jersey")
                .district("test")
                .street("Franklyn")
                .number("39")
                .build();

        var persistedBarberShop = BarberShop.builder()
                .id(barberShopId)
                .address(address)
                .name("test")
                .build();

        when(repository.findById(persistedBarberShop.getId())).thenReturn(Optional.of(persistedBarberShop));

        final var exception = assertThrows(ObjectAlreadyCreatedException.class,
                () -> service.saveAddress(address, barberShopId));

        assertThat(exception.getMessage()).isEqualTo("Address already created. Try update it");
    }

    @Test
    void shouldSaveAnLogo() {
        var barberShopId = UUID.randomUUID();

        var barberShop = BarberShop.builder()
                .id(barberShopId)
                .build();

        var uri = URI.create("https://www.teste.com/");

        var multipartFile = new MockMultipartFile("file", "test.png",
                "text/plain", "Spring Framework".getBytes());

        when(repository.findById(barberShopId)).thenReturn(Optional.of(barberShop));
        when(s3Service.uploadFile(multipartFile, barberShop.getId().toString()))
                .thenReturn(uri);

        service.saveLogo(barberShopId, multipartFile);

        verify(repository).save(barberShopCaptor.capture());

        assertThat(barberShopCaptor.getValue().getUrlLogo()).isEqualTo(uri.toString());

    }

    @Test
    void shouldDeleteAnLogoBarberShop() {
        var barberShopId = UUID.randomUUID();

        var uri = URI.create("https://www.teste.com/");

        var barberShop = BarberShop.builder()
                .id(barberShopId)
                .urlLogo(uri.toString())
                .build();

        when(repository.findById(barberShopId)).thenReturn(Optional.of(barberShop));

        service.deleteLogo(barberShopId);

        verify(repository).save(barberShopCaptor.capture());

        verify(s3Service, times(1)).deleteFile(barberShopId);

        assertThat(barberShopCaptor.getValue().getUrlLogo()).isNull();
    }
}
