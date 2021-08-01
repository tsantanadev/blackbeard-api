package br.com.blackbeard.blackbeardapi.dtos.barber;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class BarberRequest {

    @NotBlank
    private String name;

    @NotBlank
    private UUID barberShopId;
}
