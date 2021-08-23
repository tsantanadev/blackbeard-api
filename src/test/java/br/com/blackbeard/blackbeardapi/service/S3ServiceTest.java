package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.models.BarberShop;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;

    @Mock
    private AmazonS3 s3;

    @Value("${s3.bucket}")
    private String bucket;

    @Test
    void shouldCreatedURIWhenSaveFile() throws Exception {
        var uri = URI.create("https://www.teste.com/");

        var multipartFile = new MockMultipartFile("logo",
                "logo.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .build();

        var meta = new ObjectMetadata();
        InputStream inputStream = mock(InputStream.class);

        when(s3.getUrl(bucket, barberShop.getId().toString())).thenReturn(uri.toURL());
        doNothing().when(s3).putObject(bucket, "", inputStream, meta);

        var uriImage = s3Service.uploadFile(multipartFile, barberShop.getId().toString(), "logo");

        assertThat(uriImage).isEqualTo(uri);
    }

    @Test
    void shouldDeleteFile(){
        var id = UUID.randomUUID();

        s3Service.deleteFile(id);

        verify(s3).deleteObject(bucket, id.toString());
    }

}
