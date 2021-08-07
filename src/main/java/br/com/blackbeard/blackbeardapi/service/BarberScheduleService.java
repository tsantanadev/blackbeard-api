package br.com.blackbeard.blackbeardapi.service;

import br.com.blackbeard.blackbeardapi.models.BarberSchedule;
import br.com.blackbeard.blackbeardapi.repositories.BarberScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BarberScheduleService {

    private final BarberScheduleRepository barberScheduleRepository;
    private final DailyScheduleService dailyScheduleService;
    private final BarberService barberService;

    @Transactional
    public BarberSchedule save(BarberSchedule barberSchedule, UUID barberId) {
        saveDailiesSchedule(barberSchedule);

        barberSchedule.setId(UUID.randomUUID());
        final var persistedSchedule = barberScheduleRepository.save(barberSchedule);

        barberService.updateSchedule(persistedSchedule, barberId);
        return persistedSchedule;
    }

    private void saveDailiesSchedule(BarberSchedule barberSchedule) {
        barberSchedule.setSunday(dailyScheduleService.save(barberSchedule.getSunday()));
        barberSchedule.setMonday(dailyScheduleService.save(barberSchedule.getMonday()));
        barberSchedule.setTuesday(dailyScheduleService.save(barberSchedule.getTuesday()));
        barberSchedule.setWednesday(dailyScheduleService.save(barberSchedule.getWednesday()));
        barberSchedule.setThursday(dailyScheduleService.save(barberSchedule.getThursday()));
        barberSchedule.setFriday(dailyScheduleService.save(barberSchedule.getFriday()));
        barberSchedule.setSaturday(dailyScheduleService.save(barberSchedule.getSaturday()));
    }
}
