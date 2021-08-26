package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.exceptions.BarberShopImageLimitException;
import br.com.blackbeard.blackbeardapi.exceptions.FileException;
import br.com.blackbeard.blackbeardapi.service.ImageService;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.net.URI;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService service;

    @Test
    void shouldReturnCreatedWhenPostAValidRequestForImage() throws Exception {
        var barberShopId = UUID.randomUUID();

        var uri = URI.create("https://www.teste.com/");

        var multipartFile = new MockMultipartFile("image",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        when(service.saveImage(barberShopId, multipartFile)).thenReturn(uri);

        mockMvc.perform(
                multipart("/barberShop/image")
                        .file(multipartFile)
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

    }

    @Test
    void shouldReturnOKWhenDeleteValidRequestForImage() throws Exception {
        var barberShopId = UUID.randomUUID();
        var imageID = UUID.randomUUID();

        mockMvc.perform(
                delete("/barberShop/image")
                        .param("barberShopId", barberShopId.toString())
                        .param("imageId", imageID.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenPostImageFormatInvalid() throws Exception {
        var barberShopId = UUID.randomUUID();

        var multipartFile = new MockMultipartFile("image",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        var errors = "error of archive";

        when(service.saveImage(barberShopId, multipartFile)).thenThrow(FileException.class);

        mockMvc.perform(
                multipart("/barberShop/image")
                        .file(multipartFile)
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(jsonPath("$.message", is(errors)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostImageMaxUpload() throws Exception {
        var barberShopId = UUID.randomUUID();

        var multipartFile = new MockMultipartFile("image",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        var errors = "Error";

        when(service.saveImage(barberShopId, multipartFile)).thenThrow(MaxUploadSizeExceededException.class);

        mockMvc.perform(
                multipart("/barberShop/image")
                        .file(multipartFile)
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(jsonPath("$.message", is(errors)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostImageMaxLimit() throws Exception {
        var barberShopId = UUID.randomUUID();

        var multipartFile = new MockMultipartFile("image",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        var errors = "error image";

        when(service.saveImage(barberShopId, multipartFile)).thenThrow(BarberShopImageLimitException.class);

        mockMvc.perform(
                multipart("/barberShop/image")
                        .file(multipartFile)
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(jsonPath("$.message", is(errors)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestAmazonS3ExceptionWhenPostImage() throws Exception {
        var barberShopId = UUID.randomUUID();

        var multipartFile = new MockMultipartFile("image",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        var errors = "Error amazon s3";

        when(service.saveImage(barberShopId, multipartFile)).thenThrow(AmazonS3Exception.class);

        mockMvc.perform(
                multipart("/barberShop/image")
                        .file(multipartFile)
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(jsonPath("$.message", is(errors)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestAmazonClientExceptionWhenPostImage() throws Exception {
        var barberShopId = UUID.randomUUID();

        var multipartFile = new MockMultipartFile("image",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        var errors = "Error amazon client";

        when(service.saveImage(barberShopId, multipartFile)).thenThrow(AmazonClientException.class);

        mockMvc.perform(
                multipart("/barberShop/image")
                        .file(multipartFile)
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(jsonPath("$.message", is(errors)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestAmazonServiceExceptionWhenPostImage() throws Exception {
        var barberShopId = UUID.randomUUID();

        var multipartFile = new MockMultipartFile("image",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        var errors = "Error amazon service";

        when(service.saveImage(barberShopId, multipartFile)).thenThrow(AmazonServiceException.class);

        mockMvc.perform(
                multipart("/barberShop/image")
                        .file(multipartFile)
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(jsonPath("$.message", is(errors)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestFileExceptionWhenPostImage() throws Exception {
        var barberShopId = UUID.randomUUID();

        var multipartFile = new MockMultipartFile("image",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        var errors = "error of archive";

        when(service.saveImage(barberShopId, multipartFile)).thenThrow(FileException.class);

        mockMvc.perform(
                multipart("/barberShop/image")
                        .file(multipartFile)
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(jsonPath("$.message", is(errors)))
                .andExpect(status().isBadRequest());
    }
}
