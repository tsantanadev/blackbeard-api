package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.dtos.AddressRequest;
import br.com.blackbeard.blackbeardapi.dtos.BarberShopRequest;
import br.com.blackbeard.blackbeardapi.dtos.BarberShopResponse;
import br.com.blackbeard.blackbeardapi.exceptions.ObjectNotFoundException;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import br.com.blackbeard.blackbeardapi.service.BarberShopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarberShopController.class)
class BarberShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BarberShopService service;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldReturnCreatedWhenPostAValidRequest() throws Exception {
        var addressRequest = AddressRequest.builder()
                .city("Test")
                .district("Test")
                .street("Test")
                .number("42")
                .build();


        var barberShopRequest = BarberShopRequest.builder()
                .name("Test")
                .imageUrl("https://www.google.com")
                .address(addressRequest)
                .build();

        var address = Address.builder()
                .city(addressRequest.getCity())
                .district(addressRequest.getDistrict())
                .street(addressRequest.getStreet())
                .number(addressRequest.getNumber())
                .build();

        var barberShop = BarberShop.builder()
                .name(barberShopRequest.getName())
                .imageUrl(barberShopRequest.getImageUrl())
                .address(address)
                .build();

        var barberShopReturned = BarberShop.builder()
                .id(UUID.randomUUID())
                .name(barberShopRequest.getName())
                .imageUrl(barberShopRequest.getImageUrl())
                .build();

        var barberShopResponseExpected = BarberShopResponse.builder()
                .id(barberShopReturned.getId())
                .name(barberShopReturned.getName())
                .imageUrl(barberShopReturned.getImageUrl())
                .build();

        Mockito.when(service.save(barberShop)).thenReturn(barberShopReturned);

        var json = mapper.writeValueAsString(barberShopRequest);

        mockMvc.perform(post("/barberShop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string(this.mapper.writeValueAsString(barberShopResponseExpected)));
    }

    @Test
    void shouldReturnOkWhenGetByValidId() {
        var barberShopId = UUID.randomUUID();
        var barberShop = BarberShop.builder().id(barberShopId).build();

        Mockito.when(service.findById(barberShopId)).thenReturn(barberShop);

        given()
                .accept(ContentType.JSON)
                .when()
                    .get("/barberShop/{id}", barberShopId)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", is(barberShopId.toString()));
    }

    @Test
    void shouldReturnNotFoundWhenGetByInvalidId() {
        Mockito.when(service.findById(any())).thenThrow(ObjectNotFoundException.class);

        given()
                .accept(ContentType.JSON)
                .when()
                    .get("/barberShop/{id}", UUID.randomUUID())
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
