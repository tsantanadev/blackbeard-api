package br.com.blackbeard.blackbeardapi.controllers;

import br.com.blackbeard.blackbeardapi.dtos.address.AddressRequest;
import br.com.blackbeard.blackbeardapi.models.Address;
import br.com.blackbeard.blackbeardapi.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService service;

    @Captor
    ArgumentCaptor<Address> addressCaptor;

    @Captor
    ArgumentCaptor<UUID> idCaptor;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldReturnCreatedWhenPostAValidRequest() throws Exception {
        var barberShopId = UUID.randomUUID();
        var addressId = UUID.randomUUID();

        var addressRequest = AddressRequest.builder()
                .city("New jersey")
                .district("test")
                .street("Franklyn")
                .number("39")
                .build();

        var address = Address.builder()
                .city(addressRequest.getCity())
                .district(addressRequest.getDistrict())
                .number(addressRequest.getNumber())
                .street(addressRequest.getStreet())
                .build();

        var persistedAddress = Address.builder()
                .id(addressId)
                .city(addressRequest.getCity())
                .district(addressRequest.getDistrict())
                .number(addressRequest.getNumber())
                .street(addressRequest.getStreet())
                .build();

        var requestJson = mapper.writeValueAsString(addressRequest);
        var responseJson = mapper.writeValueAsString(persistedAddress);

        when(service.save(address, barberShopId)).thenReturn(persistedAddress);

        mockMvc.perform(post("/barberShop/address")
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(responseJson));

        verify(service).save(addressCaptor.capture(), idCaptor.capture());

        assertThat(addressCaptor.getValue()).isEqualTo(address);
        assertThat(idCaptor.getValue()).isEqualTo(barberShopId);
    }

    @Test
    void shouldReturnBadRequestWhenPostAnInvalidRequest() throws Exception {
        var barberShopId = UUID.randomUUID();

        var addressRequest = AddressRequest.builder().build();

        var errors = Map.of(
                "city", "must not be blank",
                "district", "must not be blank",
                "street", "must not be blank",
                "number", "must not be blank"
        );

        var requestJson = mapper.writeValueAsString(addressRequest);

        mockMvc.perform(post("/barberShop/address")
                        .param("barberShopId", barberShopId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(errors)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    void shouldReturnBadRequestWhenPostWithoutBarberShopId() throws Exception {
        var addressRequest = AddressRequest.builder().build();

        var requestJson = mapper.writeValueAsString(addressRequest);

        mockMvc.perform(post("/barberShop/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnAcceptWhenPutAValidRequest() throws Exception {
        var addressId = UUID.randomUUID();

        var addressRequest = AddressRequest.builder()
                .city("New jersey")
                .district("test")
                .street("Franklyn")
                .number("39")
                .build();

        var address = Address.builder()
                .city(addressRequest.getCity())
                .district(addressRequest.getDistrict())
                .number(addressRequest.getNumber())
                .street(addressRequest.getStreet())
                .build();

        var persistedAddress = Address.builder()
                .id(addressId)
                .city(addressRequest.getCity())
                .district(addressRequest.getDistrict())
                .number(addressRequest.getNumber())
                .street(addressRequest.getStreet())
                .build();

        var requestJson = mapper.writeValueAsString(addressRequest);

        when(service.save(address, addressId)).thenReturn(persistedAddress);

        mockMvc.perform(put("/barberShop/address")
                        .param("addressId", addressId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

}