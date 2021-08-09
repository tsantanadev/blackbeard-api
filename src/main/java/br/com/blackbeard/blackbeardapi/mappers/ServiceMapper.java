package br.com.blackbeard.blackbeardapi.mappers;

import br.com.blackbeard.blackbeardapi.dtos.service.ServiceRequest;
import br.com.blackbeard.blackbeardapi.dtos.service.ServiceResponse;
import br.com.blackbeard.blackbeardapi.models.ServiceBarber;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    private static final ModelMapper mapper = new ModelMapper();

    private ServiceMapper() {

    }

    public static ServiceBarber convertToModel(ServiceRequest request) {
        return mapper.map(request, ServiceBarber.class);
    }

    public static ServiceResponse convertToResponse(ServiceBarber service) {
        return mapper.map(service, ServiceResponse.class);
    }

}
