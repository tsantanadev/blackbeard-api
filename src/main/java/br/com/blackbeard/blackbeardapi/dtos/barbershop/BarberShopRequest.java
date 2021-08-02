package br.com.blackbeard.blackbeardapi.dtos.barbershop;

import br.com.blackbeard.blackbeardapi.dtos.address.AddressRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @Valid
    @NotNull
    private AddressRequest address;
}
