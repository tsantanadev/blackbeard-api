package br.com.blackbeard.blackbeardapi.dtos.barberDto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BarberRequestUpdate {

    @NotNull
    @NotBlank
    private String name;
}
