package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.dtos.service.ServiceRequest;
import br.com.blackbeard.blackbeardapi.dtos.service.ServiceResponse;
import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Barber;
import br.com.blackbeard.blackbeardapi.models.BarberServiceModel;
import br.com.blackbeard.blackbeardapi.service.ServiceBarberService;
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

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarberServiceController.class)
class BarberServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceBarberService service;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldReturnCreatedWhenPostAValidRequest() throws Exception {
        var barber = Barber.builder()
                .id(UUID.randomUUID())
                .build();

        var serviceRequest = ServiceRequest.builder()
                .name("teste")
                .description("teste teste teste teste teste teste")
                .price(BigDecimal.valueOf(15.5))
                .duration(BigDecimal.valueOf(30))
                .build();

        var serviceBarber = BarberServiceModel.builder()
                .name(serviceRequest.getName())
                .description(serviceRequest.getDescription())
                .duration(serviceRequest.getDuration())
                .price(serviceRequest.getPrice())
                .build();

        var serviceReturned = BarberServiceModel.builder()
                .id(UUID.randomUUID())
                .name(serviceBarber.getName())
                .description(serviceBarber.getDescription())
                .price(serviceBarber.getPrice())
                .duration(serviceBarber.getDuration())
                .barber(barber)
                .build();

        var serviceResponse = ServiceResponse.builder()
                .id(serviceReturned.getId())
                .name(serviceReturned.getName())
                .description(serviceRequest.getDescription())
                .duration(serviceRequest.getDuration())
                .price(serviceRequest.getPrice())
                .build();

        when(service.save(serviceBarber, barber.getId())).thenReturn(serviceReturned);

        var json = mapper.writeValueAsString(serviceRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/barber/service")
                                .param("barberId", barber.getId().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string(this.mapper.writeValueAsString(serviceResponse)));
    }

    @Test
    void shouldReturnCreatedWhenPostAValidRequestWithoutDescription() throws Exception {
        var barber = Barber.builder()
                .id(UUID.randomUUID())
                .build();

        var serviceRequest = ServiceRequest.builder()
                .name("teste")
                .price(BigDecimal.valueOf(15.5))
                .duration(BigDecimal.valueOf(30))
                .build();

        var serviceBarber = BarberServiceModel.builder()
                .name(serviceRequest.getName())
                .duration(serviceRequest.getDuration())
                .price(serviceRequest.getPrice())
                .build();

        var serviceReturned = BarberServiceModel.builder()
                .id(UUID.randomUUID())
                .name(serviceBarber.getName())
                .price(serviceBarber.getPrice())
                .duration(serviceBarber.getDuration())
                .barber(barber)
                .build();

        var serviceResponse = ServiceResponse.builder()
                .id(serviceReturned.getId())
                .name(serviceReturned.getName())
                .duration(serviceRequest.getDuration())
                .price(serviceRequest.getPrice())
                .build();

        when(service.save(serviceBarber, barber.getId())).thenReturn(serviceReturned);

        var json = mapper.writeValueAsString(serviceRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/barber/service")
                                .param("barberId", barber.getId().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string(this.mapper.writeValueAsString(serviceResponse)));
    }

    @Test
    void shouldReturnBadRequestAndErrorListWhenPostAnInvalidRequest() throws Exception {
        var serviceRequest = ServiceRequest.builder().build();

        var errors = Map.of(
                "name", "must not be blank"

        );

        var requestJson = mapper.writeValueAsString(serviceRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/barber/service")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(errors)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }


    @Test
    void shouldReturnBadRequestAndErrorListWhenPostAnInvalidRequestAttributeDescription() throws Exception {
        var barber = Barber.builder()
                .id(UUID.randomUUID())
                .build();

        var serviceRequest = ServiceRequest.builder()
                .name("test")
                .description("test test test test test test test test test test test test test")
                .build();

        var errors = Map.of(
                "description", "length must be between 0 and 36"

        );

        var requestJson = mapper.writeValueAsString(serviceRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/barber/service")
                                .param("barberId", barber.getId().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(errors)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    void shouldReturnOkWhenGetByValidId() throws Exception {
        var serviceBarber = BarberServiceModel.builder()
                .id(UUID.randomUUID())
                .build();

        var response = ServiceResponse.builder()
                .id(serviceBarber.getId())
                .build();

        when(service.findById(serviceBarber.getId())).thenReturn(serviceBarber);

        var json = mapper.writeValueAsString(response);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/barber/service/{id}", serviceBarber.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().string(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenGetByInvalidId() throws Exception {
        when(service.findById(any())).thenThrow(ObjectNotFoundException.class);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/barber/service/{id}", UUID.randomUUID())
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOkWhenGetAll() throws Exception {
        var barber = Barber.builder()
                .id(UUID.randomUUID())
                .build();

        var serviceBarber = BarberServiceModel.builder()
                .id(UUID.randomUUID())
                .barber(barber)
                .build();

        when(service.listAllServiceByIdBarber(barber.getId(), PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(singletonList(serviceBarber)));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/barber/service")
                                .param("barberId", barber.getId().toString())
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenGetAllIdBarberShopInvalid() throws Exception {
        var id = UUID.randomUUID();
        when(service.listAllServiceByIdBarber(id, PageRequest.of(0, 20)))
                .thenThrow(ObjectNotFoundException.class);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/barber/service")
                                .param("barberId", id.toString())
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAcceptWhenPutAValidRequest() throws Exception {
        var id = UUID.randomUUID();

        var serviceRequest = ServiceRequest.builder()
                .name("teste")
                .description("teste teste teste teste teste teste")
                .price(BigDecimal.valueOf(15.5))
                .duration(BigDecimal.valueOf(30))
                .build();

        var serviceBarber = BarberServiceModel.builder()
                .name(serviceRequest.getName())
                .description(serviceRequest.getDescription())
                .price(serviceRequest.getPrice())
                .duration(serviceRequest.getDuration())
                .build();

        var json = mapper.writeValueAsString(serviceRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/barber/service")
                                .param("serviceId", id.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isAccepted());

        verify(service, times(1)).update(serviceBarber, id);
    }

    @Test
    void shouldReturnBadRequestWhenPutAnInvalidRequest() throws Exception {
        var id = UUID.randomUUID();
        var serviceRequest = ServiceRequest.builder().build();

        var errors = Map.of(
                "name", "must not be blank"

        );

        var requestJson = mapper.writeValueAsString(serviceRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/barber/service")
                                .param("serviceId", id.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(errors)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

}
