package br.com.blackbeard.blackbeardapi.dtos.barber;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class BarberRequest {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private UUID barberShopId;
}
