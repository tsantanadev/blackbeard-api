package br.com.blackbeard.blackbeardapi.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.UUID;

@Data
@Entity
public class BarberSchedule {

    @Id
    private UUID id;

    private Integer averageTime;

    @OneToOne
    @JoinColumn(name = "sunday_id")
    private DailySchedule sunday;

    @OneToOne
    @JoinColumn(name = "monday_id")
    private DailySchedule monday;

    @OneToOne
    @JoinColumn(name = "tuesday_id")
    private DailySchedule tuesday;

    @OneToOne
    @JoinColumn(name = "wednesday_id")
    private DailySchedule wednesday;

    @OneToOne
    @JoinColumn(name = "thursday_id")
    private DailySchedule thursday;

    @OneToOne
    @JoinColumn(name = "friday_id")
    private DailySchedule friday;

    @OneToOne
    @JoinColumn(name = "saturday_id")
    private DailySchedule saturday;
}
