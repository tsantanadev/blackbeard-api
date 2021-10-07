package br.com.blackbeard.blackbeardapi.dtos.barber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BarberRequest {

    public static final int MIN_CHARACTERS = 1;
    public static final int MAX_CHARACTERS = 24;

    @NotBlank
    @Length(min = MIN_CHARACTERS, max = MAX_CHARACTERS)
    private String name;
}
