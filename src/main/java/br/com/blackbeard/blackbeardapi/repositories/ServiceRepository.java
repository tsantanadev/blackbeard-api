package br.com.blackbeard.blackbeardapi.repositories;

import br.com.blackbeard.blackbeardapi.models.ServiceBarber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceBarber, UUID> {

    Page<ServiceBarber> findAllByBarberId(UUID idBarber, Pageable pageable);
}