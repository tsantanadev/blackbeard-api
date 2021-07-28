package br.com.blackbeard.blackbeardapi.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Barber {

    @Id
    private UUID id;
    private String name;

    @ManyToOne
    private BarberShop barberShop;

    @OneToMany(mappedBy = "barber")
    private List<Service> services;
}
