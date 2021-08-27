package br.com.blackbeard.blackbeardapi.repositories;

import br.com.blackbeard.blackbeardapi.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    List<Image> findAllByBarberShopId(UUID barberShopId);
}
