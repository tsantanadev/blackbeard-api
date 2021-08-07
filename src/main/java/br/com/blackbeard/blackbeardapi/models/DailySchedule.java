package br.com.blackbeard.blackbeardapi.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity
public class DailySchedule {

    @Id
    private UUID id;

    private boolean outOfWork;

    private LocalTime startTime;
    private LocalTime startIntervalTime;
    private LocalTime endIntervalTime;
    private LocalTime endTime;
}
