package br.com.blackbeard.blackbeardapi.dtos.barbershop;

import br.com.blackbeard.blackbeardapi.models.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarberShopResponse {
    private UUID id;
    private String name;
    private String imageUrl;
    private Address address;
}
