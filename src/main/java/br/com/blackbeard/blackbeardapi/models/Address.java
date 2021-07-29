package br.com.blackbeard.blackbeardapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
