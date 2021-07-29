package br.com.blackbeard.blackbeardapi.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class Address {

    @Id
    private UUID id;
    private String city;
    private String district;
    private String street;
    private String number;

    public void update(Address address) {
        this.city = address.getCity();
        this.district = address.getDistrict();
        this.street = address.getStreet();
        this.number = address.getNumber();
    }

    public void generateId() {
        this.id = UUID.randomUUID();
    }
}
