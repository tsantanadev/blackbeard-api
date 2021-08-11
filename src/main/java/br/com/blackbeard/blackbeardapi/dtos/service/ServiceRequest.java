package br.com.blackbeard.blackbeardapi.dtos.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Length(min = 30, max = 500)
    private String description;

    private BigDecimal price;
    private BigDecimal duration;
}
