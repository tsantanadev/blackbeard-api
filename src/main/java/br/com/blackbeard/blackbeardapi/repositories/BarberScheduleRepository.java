package br.com.blackbeard.blackbeardapi.repositories;

import br.com.blackbeard.blackbeardapi.models.BarberSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BarberScheduleRepository extends JpaRepository<BarberSchedule, UUID> {

}