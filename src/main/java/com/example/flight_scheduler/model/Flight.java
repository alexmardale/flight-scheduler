package com.example.flight_scheduler.model;

import com.example.flight_scheduler.validation.FlightDateTimeConstraint;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Data
@FlightDateTimeConstraint
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Enumerated(value = EnumType.STRING)
    private FlightStatus flightStatus;
}
