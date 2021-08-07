package br.com.blackbeard.blackbeardapi.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
public class Service {

    @Id
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;

    @ManyToOne
    private Barber barber;
}