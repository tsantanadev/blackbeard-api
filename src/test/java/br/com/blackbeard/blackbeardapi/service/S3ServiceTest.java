package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.exceptions.FileException;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        var file = new File("src/test/images/teste.png");
        var input = new FileInputStream(file);
        var multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .build();

        when(s3.getUrl(bucket, barberShop.getId().toString())).thenReturn(uri.toURL());

        var uriImage = s3Service.uploadFile(multipartFile, barberShop.getId().toString(), "logo");

        assertThat(uriImage).isEqualTo(uri);
    }

    @Test
    void shouldDeleteFile() {
        var id = UUID.randomUUID();

        s3Service.deleteFile(id);

        verify(s3).deleteObject(bucket, id.toString());
    }

    @Test
    void shouldThrowExceptionWhenSaveAFileThatIsNotJpgOrPng() throws Exception {
        var file = new File("src/test/images/teste.pdf");
        var input = new FileInputStream(file);
        var multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        var exception = assertThrows(FileException.class,
                () -> s3Service.uploadFile(multipartFile, "", "logo"));

        assertThat(exception).hasMessage("only accept images PNG and JPG");
    }

}
