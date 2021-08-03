package br.com.blackbeard.blackbeardapi.dtos.barber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BarberRequest {

    @NotBlank
    private String name;

    @NotNull
    private UUID barberShopId;
}
