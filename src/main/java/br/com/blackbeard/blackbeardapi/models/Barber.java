package br.com.blackbeard.blackbeardapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Barber {

    @Id
    private UUID id;
    private String name;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne
    private BarberShop barberShop;

    @JsonIgnore
    @OneToMany(mappedBy = "barber")
    private List<Service> services;

    public void generateId() {
        this.id = UUID.randomUUID();
    }

    public void update(Barber barber) {
        this.name = barber.getName();
    }
}
