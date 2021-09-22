package br.com.blackbeard.blackbeardapi.controllers;


import br.com.blackbeard.blackbeardapi.dtos.barber.BarberRequest;
import br.com.blackbeard.blackbeardapi.dtos.barber.BarberResponse;
import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Barber;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.service.BarberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarberController.class)
class BarberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BarberService service;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldReturnCreatedWhenPostAValidRequest() throws Exception {
        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .build();

        var barberRequest = BarberRequest.builder()
                .name("teste")
                .build();

        var barber = Barber.builder()
                .name(barberRequest.getName())
                .build();

        var barberReturned = Barber.builder()
                .id(UUID.randomUUID())
                .name(barber.getName())
                .barberShop(barberShop)
                .build();

        var barberResponse = BarberResponse.builder()
                .id(barberReturned.getId())
                .name(barberReturned.getName())
                .build();

        when(service.save(barber, barberShop.getId())).thenReturn(barberReturned);

        var json = mapper.writeValueAsString(barberRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/barber")
                        .param("idBarberShop", barberShop.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string(this.mapper.writeValueAsString(barberResponse)));
    }

    @Test
    void shouldReturnBadRequestAndErrorListWhenPostAnInvalidRequest() throws Exception {
        var barberRequest = BarberRequest.builder().build();

        var errors = Map.of(
                "name", "must not be blank"
        );

        var requestJson = mapper.writeValueAsString(barberRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/barber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(errors)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    void shouldReturnOkWhenGetByValidId() throws Exception {
        var barber = Barber.builder()
                .id(UUID.randomUUID())
                .build();

        var response = BarberResponse.builder()
                .id(barber.getId())
                .build();

        when(service.findById(barber.getId())).thenReturn(barber);

        var json = mapper.writeValueAsString(response);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/barber/{id}", barber.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().string(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenGetByInvalidId() throws Exception {
        when(service.findById(any())).thenThrow(ObjectNotFoundException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/barber/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOkWhenGetAll() throws Exception {
        var barberShop = BarberShop.builder()
                .id(UUID.randomUUID())
                .build();

        var barber = Barber.builder()
                .id(UUID.randomUUID())
                .barberShop(barberShop)
                .build();

        when(service.listAllBarberByIdBarberShop(barberShop.getId(), PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(singletonList(barber)));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/barber")
                        .param("idBarberShop", barberShop.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenGetAllIdBarberShopInvalid() throws Exception {
        var id = UUID.randomUUID();
        when(service.listAllBarberByIdBarberShop(id, PageRequest.of(0, 20)))
                .thenThrow(ObjectNotFoundException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/barber")
                        .param("idBarberShop", id.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAcceptWhenPutAValidRequest() throws Exception {
        var id = UUID.randomUUID();

        var barberRequest = BarberRequest.builder()
                .name("teste")
                .build();

        var barber = Barber.builder()
                .name(barberRequest.getName())
                .build();

        var json = mapper.writeValueAsString(barberRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/barber")
                        .param("idBarber", id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted());

        verify(service, times(1)).update(barber, id);
    }

    @Test
    void shouldReturnBadRequestWhenPutAnInvalidRequest() throws Exception {
        var id = UUID.randomUUID();
        var barberRequest = BarberRequest.builder().build();

        var errors = Map.of(
                "name", "must not be blank"
        );

        var requestJson = mapper.writeValueAsString(barberRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/barber")
                        .param("idBarber", id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(errors)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }
}
