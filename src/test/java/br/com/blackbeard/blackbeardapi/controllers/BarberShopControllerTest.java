package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.dtos.barbershop.BarberShopRequest;
import br.com.blackbeard.blackbeardapi.dtos.barbershop.BarberShopResponse;
import br.com.blackbeard.blackbeardapi.exceptions.FileException;
import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.service.BarberShopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarberShopController.class)
class BarberShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BarberShopService service;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldReturnCreatedWhenPostAValidRequest() throws Exception {
        var barberShopRequest = BarberShopRequest.builder()
                .name("Test")
                .build();

        var barberShop = BarberShop.builder()
                .name(barberShopRequest.getName())
                .build();

        var barberShopReturned = BarberShop.builder()
                .id(UUID.randomUUID())
                .name(barberShopRequest.getName())
                .build();

        var barberShopResponseExpected = BarberShopResponse.builder()
                .id(barberShopReturned.getId())
                .name(barberShopReturned.getName())
                .build();

        when(service.save(barberShop)).thenReturn(barberShopReturned);

        var json = mapper.writeValueAsString(barberShopRequest);

        mockMvc.perform(post("/barberShop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string(this.mapper.writeValueAsString(barberShopResponseExpected)));
    }

    @Test
    void shouldReturnBadRequestAndErrorListWhenPostAnInvalidRequest() throws Exception {
        var barberShopRequest = BarberShopRequest.builder().build();

        var errors = Map.of(
                "name", "must not be blank"
        );

        var requestJson = mapper.writeValueAsString(barberShopRequest);

        mockMvc.perform(post("/barberShop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(errors)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    void shouldReturnAcceptWhenPutAValidRequest() throws Exception {
        var barberShopRequest = BarberShopRequest.builder()
                .name("Test")
                .build();

        var barberShop = BarberShop.builder()
                .name(barberShopRequest.getName())
                .build();

        var barberShopId = UUID.randomUUID();

        var json = mapper.writeValueAsString(barberShopRequest);

        mockMvc.perform(put("/barberShop")
                .param("barberShopId", barberShopId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isAccepted());

        verify(service).update(barberShop, barberShopId);
    }

    @Test
    void shouldReturnBadRequestWhenPutAnInvalidRequest() throws Exception {
        var barberShopRequest = BarberShopRequest.builder().build();
        var barberShopId = UUID.randomUUID();

        var errors = Map.of(
                "name", "must not be blank"
        );

        var requestJson = mapper.writeValueAsString(barberShopRequest);

        mockMvc.perform(put("/barberShop")
                .param("barberShopId", barberShopId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(errors)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    void shouldReturnOkWhenGetByValidId() throws Exception {
        var barberShopId = UUID.randomUUID();
        var barberShop = BarberShop.builder().id(barberShopId).build();

        var expectedResponse = BarberShopResponse.builder().id(barberShopId).build();

        when(service.findById(barberShopId)).thenReturn(barberShop);

        var json = mapper.writeValueAsString(expectedResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/barberShop/{id}", barberShopId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().string(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenGetByInvalidId() throws Exception {
        when(service.findById(any())).thenThrow(ObjectNotFoundException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/barberShop/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOkWhenGetAll() throws Exception {
        var barberShopId = UUID.randomUUID();
        var barberShop = BarberShop.builder().id(barberShopId).build();

        when(service.listAll(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(singletonList(barberShop)));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/barberShop")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCreatedWhenPostValidRequestForLogo() throws Exception {
        var barberShopId = UUID.randomUUID();

        var uri = URI.create("https://www.teste.com/");

        var multipartFile = new MockMultipartFile("logo",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        when(service.saveLogo(barberShopId, multipartFile)).thenReturn(uri);

        mockMvc.perform(
                multipart("/barberShop/logo")
                        .file(multipartFile)
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }


    @Test
    void shouldReturnOKWhenDeleteValidRequestForLogo() throws Exception {
        var barberShopId = UUID.randomUUID();

        mockMvc.perform(
                delete("/barberShop/logo")
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenPostImageFormatInvalid() throws Exception {
        var barberShopId = UUID.randomUUID();

        var multipartFile = new MockMultipartFile("logo",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        var errors = "error of archive";

        when(service.saveLogo(barberShopId, multipartFile)).thenThrow(FileException.class);

        mockMvc.perform(
                multipart("/barberShop/logo")
                        .file(multipartFile)
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(jsonPath("$.message", is(errors)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPostImageMaxUpload() throws Exception {
        var barberShopId = UUID.randomUUID();

        var multipartFile = new MockMultipartFile("logo",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes());

        var errors = "Image size exceeded";

        when(service.saveLogo(barberShopId, multipartFile)).thenThrow(MaxUploadSizeExceededException.class);

        mockMvc.perform(
                multipart("/barberShop/logo")
                        .file(multipartFile)
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(jsonPath("$.message", is(errors)))
                .andExpect(status().isBadRequest());
    }
}
