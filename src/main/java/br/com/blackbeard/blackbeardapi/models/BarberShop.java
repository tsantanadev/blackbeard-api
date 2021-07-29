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
public class BarberShop {

    @Id
    private UUID id;
    private String name;
    private String imageUrl;

    @ManyToOne
    private Address address;

    @CreatedDate
    private LocalDateTime createDateTime;

    @JsonIgnore
    @OneToMany(mappedBy = "barberShop")
    private List<Barber> barber;

    public void update(BarberShop barberShop) {
        this.name = barberShop.getName();
        this.imageUrl = barberShop.getImageUrl();
    }

    public void generateId() {
        this.id = UUID.randomUUID();
    }

}
