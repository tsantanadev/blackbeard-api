package br.com.blackbeard.blackbeardapi.dtos.barber;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BarberRequestUpdate {

    @NotBlank
    private String name;
}
