package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.FileException;
import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.models.Image;
import br.com.blackbeard.blackbeardapi.repositories.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Spy
    @InjectMocks
    private ImageService service;

    @Mock
    private ImageRepository repository;

    @Mock
    private S3Service s3Service;

    @Captor
    ArgumentCaptor<Image> imageCaptor;

    @Test
    void shouldCreateAnImage() {
        var uri = URI.create("https://www.teste.com/");

        var multipartFile = new MockMultipartFile("image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .build();

        var image = Image.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();

        when(repository.findAllByBarberShopId(barberShop.getId()))
                .thenReturn(singletonList(image));

        when(service.createdImage(barberShop)).thenReturn(image);

        when(s3Service.uploadFile(multipartFile, image.getId().toString(), "image")).thenReturn(uri);

        var imageURI = service.saveImages(barberShop, multipartFile);

        verify(repository, times(1)).save(imageCaptor.capture());

        assertThat(uri).isEqualTo(imageURI);
        assertThat(imageCaptor.getValue().getUrl()).isEqualTo(imageURI.toString());

    }

    @Test
    void shouldFindAImageShopById() {
        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .build();

        var image = Image.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();

        when(repository.findById(image.getId())).thenReturn(Optional.of(image));

        var result = service.findById(image.getId());

        assertThat(result).isEqualTo(image);
        verify(repository, times(1)).findById(image.getId());
    }

    @Test
    void shouldThrowExceptionWhenFindAImageById() {
        var imageID = UUID.randomUUID();

        when(repository.findById(imageID)).thenReturn(Optional.empty());

        var exception = assertThrows(ObjectNotFoundException.class,
                () -> service.findById(imageID));

        assertThat(exception).hasMessage("Object not found");
    }

    @Test
    void shouldCreatedNewImage() {
        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .build();

        var image = service.createdImage(barberShop);

        assertThat(image.getId()).isNotNull();
    }

    @Test
    void shouldDeleteImage() {
        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .build();

        var imageId = UUID.randomUUID();

        var image = Image.builder()
                .id(imageId)
                .barberShop(barberShop)
                .build();

        when(repository.findById(imageId)).thenReturn(Optional.of(image));

        service.deleteImage(barberShop, imageId);

        verify(s3Service, times(1)).deleteFile(image.getId());
        verify(repository, times(1)).delete(image);
    }

    @Test
    void shouldThrowExceptionWhenDeleteAImage() {
        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .build();

        var imageId = UUID.randomUUID();

        var image = Image.builder()
                .id(imageId)
                .barberShop(BarberShop.builder()
                        .id(UUID.randomUUID())
                        .build())
                .build();

        when(repository.findById(imageId)).thenReturn(Optional.of(image));

        var exception = assertThrows(FileException.class,
                () -> service.deleteImage(barberShop, imageId));


        assertThat(exception).hasMessage("image does not belong to this barbershop");
    }

    @Test
    void shouldThrowExceptionWhenSaveImageAndListImageIsGreaterThanFive() {
        var multipartFile = new MockMultipartFile("image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .build();

        var image = Image.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();

        var image2 = Image.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();

        var image3 = Image.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();

        var image4 = Image.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();

        var image5 = Image.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();

        var image6 = Image.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();

        when(repository.findAllByBarberShopId(barberShop.getId()))
                .thenReturn(Arrays.asList(image, image2, image3, image4, image5, image6));

        var exception = assertThrows(FileException.class,
                () -> service.saveImages(barberShop, multipartFile));


        assertThat(exception).hasMessage("limit of images exceeded");
    }


}



