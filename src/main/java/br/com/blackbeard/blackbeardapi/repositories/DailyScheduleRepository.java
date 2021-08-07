package br.com.blackbeard.blackbeardapi.repositories;

import br.com.blackbeard.blackbeardapi.models.DailySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DailyScheduleRepository extends JpaRepository<DailySchedule, UUID> {

}