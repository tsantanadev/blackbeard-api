package br.com.blackbeard.blackbeardapi.mappers;

import br.com.blackbeard.blackbeardapi.dtos.barber.BarberScheduleRequest;
import br.com.blackbeard.blackbeardapi.models.BarberSchedule;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BarberScheduleMapper {

    private static final ModelMapper mapper = new ModelMapper();

    private BarberScheduleMapper() {
    }

    public static BarberSchedule convertToModel(BarberScheduleRequest request) {
        return mapper.map(request, BarberSchedule.class);
    }
}
