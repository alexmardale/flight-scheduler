package com.example.flight_scheduler.service;

import com.example.flight_scheduler.dto.CreateFlightDto;
import com.example.flight_scheduler.dto.GetFlightDto;
import com.example.flight_scheduler.model.Flight;
import com.example.flight_scheduler.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.example.flight_scheduler.utils.FlightUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FlightServiceIntegrationTest {
    private static final Long ID = 1L;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightService flightService;

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
}
