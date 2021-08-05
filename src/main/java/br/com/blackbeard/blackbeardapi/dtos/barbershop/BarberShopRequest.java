package br.com.blackbeard.blackbeardapi.dtos.barbershop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BarberShopRequest {

    @NotBlank
    private String name;

}
