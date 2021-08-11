package br.com.blackbeard.blackbeardapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarberServiceModel {

    @Id
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal duration;

    @ManyToOne
    private Barber barber;

    public void generateId() {
        this.id = UUID.randomUUID();
    }

    public void update(BarberServiceModel service) {
        this.name = service.getName();
        this.description = service.getDescription();
        this.price = service.getPrice();
        this.duration = service.getDuration();
    }
}