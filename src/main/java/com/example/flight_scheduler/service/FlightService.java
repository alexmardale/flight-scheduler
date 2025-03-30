package com.example.flight_scheduler.service;

import com.example.flight_scheduler.dto.CreateFlightDto;
import com.example.flight_scheduler.dto.GetFlightDto;
import com.example.flight_scheduler.mapper.FlightMapper;
import com.example.flight_scheduler.model.Flight;
import com.example.flight_scheduler.repository.FlightRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
public class FlightService {
    private final FlightRepository flightRepository;

    private final FlightMapper flightMapper;

    @Autowired
    public FlightService(FlightRepository flightRepository, FlightMapper flightMapper) {
        this.flightRepository = flightRepository;
        this.flightMapper = flightMapper;
    }

    /**
     * Save provided flight to database
     *
     * @param createFlightDto the flight to save
     * @return saved flight with id
     */
    public GetFlightDto createFlight(@Valid CreateFlightDto createFlightDto) {
        Flight flight = flightRepository.save(flightMapper.toEntity(createFlightDto));
        log.info("Saved flight: {}", flight);

        return flightMapper.toGetFlightDto(flight);
    }
}
