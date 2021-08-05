package br.com.blackbeard.blackbeardapi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BarberShop {

    @Id
    private UUID id;
    private String name;
    private String urlLogo;

    @ManyToOne
    private Address address;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @CreatedDate
    private LocalDateTime createdDate;

    @JsonIgnore
    @OneToMany(mappedBy = "barberShop")
    private List<Barber> barber;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "barberShop")
    private List<Image> images;

    public void update(BarberShop barberShop) {
        this.name = barberShop.getName();
        this.urlLogo = barberShop.getUrlLogo();
    }

    public void generateId() {
        this.id = UUID.randomUUID();
    }
}

