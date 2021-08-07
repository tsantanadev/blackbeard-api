package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.models.DailySchedule;
import br.com.blackbeard.blackbeardapi.repositories.DailyScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DailyScheduleService {

    private final DailyScheduleRepository repository;

    @Transactional
    public DailySchedule save(DailySchedule dailySchedule) {
        dailySchedule.setId(UUID.randomUUID());
        return repository.save(dailySchedule);
    }
}
