package br.com.blackbeard.blackbeardapi.dtos.barbershop;

import br.com.blackbeard.blackbeardapi.models.Address;
import lombok.Data;

import java.util.UUID;

@Data
public class BarberShopResponse {
    private UUID id;
    private String name;
    private String imageUrl;
    private Address address;
}