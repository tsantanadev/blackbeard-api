package br.com.blackbeard.blackbeardapi.mappers;

import br.com.blackbeard.blackbeardapi.dtos.barberShop.BarberShopRequest;
import br.com.blackbeard.blackbeardapi.dtos.barberShop.BarberShopResponse;
import br.com.blackbeard.blackbeardapi.models.BarberShop;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BarberShopMapper {
    private static final ModelMapper mapper = new ModelMapper();

    private BarberShopMapper() {
    }

    public static BarberShop convertToModel(BarberShopRequest request) {
        return mapper.map(request, BarberShop.class);
    }

    public static BarberShopResponse convertToResponse(BarberShop barberShop) {
        return mapper.map(barberShop, BarberShopResponse.class);
    }
}


