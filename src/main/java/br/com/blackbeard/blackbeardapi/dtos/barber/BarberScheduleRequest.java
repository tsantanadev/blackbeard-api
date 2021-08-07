package br.com.blackbeard.blackbeardapi.dtos.barber;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class BarberScheduleRequest {

    @NotNull
    @Min(1)
    private Integer averageTime;

    @NotNull
    private DailyScheduleRequest sunday;

    @NotNull
    private DailyScheduleRequest monday;

    @NotNull
    private DailyScheduleRequest tuesday;

    @NotNull
    private DailyScheduleRequest wednesday;

    @NotNull
    private DailyScheduleRequest thursday;

    @NotNull
    private DailyScheduleRequest friday;

    @NotNull
    private DailyScheduleRequest saturday;
}
