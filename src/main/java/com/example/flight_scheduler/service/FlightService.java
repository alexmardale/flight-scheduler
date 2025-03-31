package com.example.flight_scheduler.service;

import com.example.flight_scheduler.dto.CreateFlightDto;
import com.example.flight_scheduler.dto.GetFlightDto;
import com.example.flight_scheduler.exception.FlightNotFoundException;
import com.example.flight_scheduler.mapper.FlightMapper;
import com.example.flight_scheduler.model.Flight;
import com.example.flight_scheduler.model.FlightStatus;
import com.example.flight_scheduler.repository.FlightRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

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

    /**
     * Retrieve the flight specified by id
     *
     * @param id the id of the flight to retrieve
     * @return the flight specified by id
     * @throws FlightNotFoundException if no flight is found by the specified id
     */
    public GetFlightDto getFlight(Long id) throws FlightNotFoundException {
        Optional<Flight> flightOptional = flightRepository.findById(id);

        if (flightOptional.isEmpty()) {
            log.info("Flight not found for id {}", id);
            throw new FlightNotFoundException();
        } else {
            log.info("Flight found for id {}", id);
            return flightMapper.toGetFlightDto(flightOptional.get());
        }
    }

    /**
     * Retrieve a list of flights
     *
     * @return a list of flights
     */
    public List<GetFlightDto> getFlights() {
        return flightRepository.findAll().stream().map(flightMapper::toGetFlightDto).toList();
    }

    /**
     * Cancel flight specified by id
     *
     * @param id the id of the flight to cancel
     * @return the cancelled flight
     * @throws FlightNotFoundException if no flight is found by the specified id
     */
    public GetFlightDto cancelFlight(Long id) throws FlightNotFoundException {
        Optional<Flight> flightOptional = flightRepository.findById(id);

        if (flightOptional.isEmpty()) {
            log.info("Flight not found for id {}", id);
            throw new FlightNotFoundException();
        } else {
            Flight flight = flightOptional.get();
            flight.setFlightStatus(FlightStatus.CANCELLED);
            flightRepository.save(flight);

            log.info("Flight with id {} has been cancelled", id);
            return flightMapper.toGetFlightDto(flight);
        }
    }
}
