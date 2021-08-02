package br.com.blackbeard.blackbeardapi.mappers;

import br.com.blackbeard.blackbeardapi.dtos.address.AddressRequest;
import br.com.blackbeard.blackbeardapi.models.Address;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    private static final ModelMapper mapper = new ModelMapper();

    private AddressMapper() {
    }

    public static Address convertToModel(AddressRequest request) {
        return mapper.map(request, Address.class);
    }

}
