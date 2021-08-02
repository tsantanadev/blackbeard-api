package br.com.blackbeard.blackbeardapi.dtos.barbershop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BarberShopRequest {

    @NotBlank
    private String name;

    @URL
    @NotBlank
    private String imageUrl;

}
