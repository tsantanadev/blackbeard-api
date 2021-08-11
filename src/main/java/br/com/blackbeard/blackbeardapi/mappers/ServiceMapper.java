package br.com.blackbeard.blackbeardapi.mappers;

import br.com.blackbeard.blackbeardapi.dtos.service.ServiceRequest;
import br.com.blackbeard.blackbeardapi.dtos.service.ServiceResponse;
import br.com.blackbeard.blackbeardapi.models.BarberServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    private static final ModelMapper mapper = new ModelMapper();

    private ServiceMapper() {

    }

    public static BarberServiceModel convertToModel(ServiceRequest request) {
        return mapper.map(request, BarberServiceModel.class);
    }

    public static ServiceResponse convertToResponse(BarberServiceModel service) {
        return mapper.map(service, ServiceResponse.class);
    }

}
