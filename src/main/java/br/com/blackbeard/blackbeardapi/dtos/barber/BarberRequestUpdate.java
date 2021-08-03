package br.com.blackbeard.blackbeardapi.dtos.barber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BarberRequestUpdate {

    @NotBlank
    private String name;
}
