package br.com.blackbeard.blackbeardapi.mappers;

import br.com.blackbeard.blackbeardapi.dtos.barberDto.BarberRequest;
import br.com.blackbeard.blackbeardapi.dtos.barberDto.BarberRequestUpdate;
import br.com.blackbeard.blackbeardapi.dtos.barberDto.BarberResponse;
import br.com.blackbeard.blackbeardapi.models.Barber;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BarberMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Barber convertToModel(BarberRequest request) {
        return mapper.map(request, Barber.class);
    }

    public static Barber convertToModel(BarberRequestUpdate request) {
        return mapper.map(request, Barber.class);
    }

    public static BarberResponse convertToResponse(Barber barber) {
        return mapper.map(barber, BarberResponse.class);
    }
}
