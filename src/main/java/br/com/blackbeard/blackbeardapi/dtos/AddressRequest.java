package br.com.blackbeard.blackbeardapi.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddressRequest {

    @NotBlank
    private String city;

    @NotBlank
    private String district;

    @NotBlank
    private String street;

    @NotBlank
    private String number;
}