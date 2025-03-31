package com.example.flight_scheduler.dto;

import com.example.flight_scheduler.model.FlightStatus;
import lombok.Data;

@Data
public class SearchFlightDto {
    private String flightNumber;

    private String departure;

    private String destination;

    private FlightStatus flightStatus;
}
