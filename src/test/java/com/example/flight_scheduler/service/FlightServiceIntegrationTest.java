package com.example.flight_scheduler.service;

import com.example.flight_scheduler.dto.CreateFlightDto;
import com.example.flight_scheduler.dto.GetFlightDto;
import com.example.flight_scheduler.exception.FlightNotFoundException;
import com.example.flight_scheduler.model.Flight;
import com.example.flight_scheduler.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.example.flight_scheduler.utils.FlightUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FlightServiceIntegrationTest {
    private static final Long ID = 1L;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightService flightService;

    @BeforeEach
    void init() {
        flightRepository.deleteAll();
    }

    @Test
    void createFlightTest() {
        CreateFlightDto createFlightDto = buildCreateFlightDto();

        GetFlightDto actualGetFlightDto = flightService.createFlight(createFlightDto);

        assertEquals(buildGetFlightDto(), actualGetFlightDto);

        // Check flight was saved
        List<Flight> flights = flightRepository.findAll();
        assertEquals(1, flights.size());
        Flight flight = flights.getFirst();
        assertEquals(buildFlight(ID), flight);
    }

    @Test
    void getFlightNominalTest() throws FlightNotFoundException {
        Flight flight = flightRepository.save(buildFlight(null));
        Long id = flight.getId();

        GetFlightDto actualGetFlightDto = flightService.getFlight(id);

        GetFlightDto expectedGetFlightDto = buildGetFlightDto();
        expectedGetFlightDto.setId(id);

        assertEquals(expectedGetFlightDto, actualGetFlightDto);
    }

    @Test
    void getFlightNotFoundTest() {
        assertThrows(
                FlightNotFoundException.class,
                () -> flightService.getFlight(ID));
    }
}
