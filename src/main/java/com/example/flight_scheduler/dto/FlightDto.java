package com.example.flight_scheduler.dto;

import com.example.flight_scheduler.validation.FlightDateTimeConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@FlightDateTimeConstraint
public abstract class FlightDto {
    @NotBlank
    private String flightNumber;

    @NotBlank
    private String departure;

    @NotBlank
    private String destination;

    @NotNull
    private OffsetDateTime departureTime;

    @NotNull
    private OffsetDateTime arrivalTime;
}
