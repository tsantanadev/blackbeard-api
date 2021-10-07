package br.com.blackbeard.blackbeardapi.repositories;

import br.com.blackbeard.blackbeardapi.models.Barber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BarberRepository extends JpaRepository<Barber, UUID> {

    Page<Barber> findAllByBarberShopId(UUID idBarberShop, Pageable pageable);

    List<Barber> findAllByBarberShopId(UUID idBarberShop);
}