package com.example.flight_scheduler.service;

import com.example.flight_scheduler.dto.CreateFlightDto;
import com.example.flight_scheduler.dto.GetFlightDto;
import com.example.flight_scheduler.dto.SearchFlightDto;
import com.example.flight_scheduler.exception.FlightNotFoundException;
import com.example.flight_scheduler.model.Flight;
import com.example.flight_scheduler.model.FlightStatus;
import com.example.flight_scheduler.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static com.example.flight_scheduler.utils.FlightUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    @Test
    void getFlightsEmptyTest() {
        assertTrue(flightService.getFlights().isEmpty());
    }

    @Test
    void getFlightsOneFlightTest() {
        Flight flight = flightRepository.save(buildFlight(null));
        Long id = flight.getId();

        List<GetFlightDto> actualGetFlightDtoList = flightService.getFlights();

        assertEquals(1, actualGetFlightDtoList.size());

        GetFlightDto expectedGetFlightDto = buildGetFlightDto();
        expectedGetFlightDto.setId(id);
        assertEquals(buildGetFlightDto(), actualGetFlightDtoList.getFirst());
    }

    @Test
    void getFlightsMultipleFlightsTest() {
        Flight flight1 = flightRepository.save(buildFlight(null));
        Long id1 = flight1.getId();

        Flight flight2 = flightRepository.save(buildFlight(null));
        Long id2 = flight2.getId();

        Flight flight3 = flightRepository.save(buildFlight(null));
        Long id3 = flight3.getId();

        List<GetFlightDto> actualGetFlightDtoList = flightService.getFlights();

        assertEquals(3, actualGetFlightDtoList.size());

        GetFlightDto expectedGetFlightDto1 = buildGetFlightDto();
        expectedGetFlightDto1.setId(id1);

        GetFlightDto expectedGetFlightDto2 = buildGetFlightDto();
        expectedGetFlightDto2.setId(id2);

        GetFlightDto expectedGetFlightDto3 = buildGetFlightDto();
        expectedGetFlightDto3.setId(id3);

        assertTrue(actualGetFlightDtoList.containsAll(List.of(expectedGetFlightDto1, expectedGetFlightDto2, expectedGetFlightDto3)));
    }

    @Test
    void cancelFlightNominalTest() throws FlightNotFoundException {
        Flight flight = flightRepository.save(buildFlight(null));
        Long id = flight.getId();

        GetFlightDto actualGetFlightDto = flightService.cancelFlight(id);

        GetFlightDto expectedGetFlightDto = buildGetFlightDto();
        expectedGetFlightDto.setId(id);
        expectedGetFlightDto.setFlightStatus(FlightStatus.CANCELLED);

        assertEquals(expectedGetFlightDto, actualGetFlightDto);

        Flight expectedFlight = buildFlight(id);
        expectedFlight.setFlightStatus(FlightStatus.CANCELLED);
        Optional<Flight> flightOptional = flightRepository.findById(id);
        assertTrue(flightOptional.isPresent());
        assertEquals(expectedFlight, flightOptional.get());
    }

    @Test
    void cancelFlightNotFoundTest() {
        assertThrows(
                FlightNotFoundException.class,
                () -> flightService.cancelFlight(ID));
    }

    @Test
    void searchFlightsEmptyTest() {
        assertTrue(flightService.searchFlights(buildSearchFlightDto()).isEmpty());
    }

    @Test
    void searchFlightsOneFlightTest() {
        Flight flight = flightRepository.save(buildFlight(null));
        Long id = flight.getId();

        List<GetFlightDto> actualGetFlightDtoList = flightService.searchFlights(buildSearchFlightDto());

        assertEquals(1, actualGetFlightDtoList.size());

        GetFlightDto expectedGetFlightDto = buildGetFlightDto();
        expectedGetFlightDto.setId(id);
        assertEquals(buildGetFlightDto(), actualGetFlightDtoList.getFirst());
    }

    @Test
    void searchFlightsMultipleFlightsTest() {
        Flight flight1 = flightRepository.save(buildFlight(null));
        Long id1 = flight1.getId();

        Flight flight2 = flightRepository.save(buildFlight(null));
        Long id2 = flight2.getId();

        Flight flight3 = flightRepository.save(buildFlight(null));
        Long id3 = flight3.getId();

        List<GetFlightDto> actualGetFlightDtoList = flightService.searchFlights(buildSearchFlightDto());

        assertEquals(3, actualGetFlightDtoList.size());

        GetFlightDto expectedGetFlightDto1 = buildGetFlightDto();
        expectedGetFlightDto1.setId(id1);

        GetFlightDto expectedGetFlightDto2 = buildGetFlightDto();
        expectedGetFlightDto2.setId(id2);

        GetFlightDto expectedGetFlightDto3 = buildGetFlightDto();
        expectedGetFlightDto3.setId(id3);

        assertTrue(actualGetFlightDtoList.containsAll(List.of(expectedGetFlightDto1, expectedGetFlightDto2, expectedGetFlightDto3)));
    }

    @Test
    void searchFlightsMultipleFlightsFewerCriteriaTest() {
        Flight flight1 = flightRepository.save(buildFlight(null));
        Long id1 = flight1.getId();

        Flight flight2 = flightRepository.save(buildFlight(null));
        Long id2 = flight2.getId();

        Flight flight3 = flightRepository.save(buildFlight(null));
        Long id3 = flight3.getId();

        SearchFlightDto searchFlightDto = buildSearchFlightDto();
        searchFlightDto.setDeparture(null);
        searchFlightDto.setDestination(null);
        List<GetFlightDto> actualGetFlightDtoList = flightService.searchFlights(searchFlightDto);

        assertEquals(3, actualGetFlightDtoList.size());

        GetFlightDto expectedGetFlightDto1 = buildGetFlightDto();
        expectedGetFlightDto1.setId(id1);

        GetFlightDto expectedGetFlightDto2 = buildGetFlightDto();
        expectedGetFlightDto2.setId(id2);

        GetFlightDto expectedGetFlightDto3 = buildGetFlightDto();
        expectedGetFlightDto3.setId(id3);

        assertTrue(actualGetFlightDtoList.containsAll(List.of(expectedGetFlightDto1, expectedGetFlightDto2, expectedGetFlightDto3)));
    }

    @Test
    void searchFlightsMultipleFlightsFewerMatchesTest() {
        Flight flight1 = flightRepository.save(buildFlight(null));
        Long id1 = flight1.getId();

        Flight flight2 = flightRepository.save(buildFlight(null));
        Long id2 = flight2.getId();

        Flight flight3 = buildFlight(null);
        flight3.setFlightNumber("somethingDifferent");
        flightRepository.save(flight3);

        List<GetFlightDto> actualGetFlightDtoList = flightService.searchFlights(buildSearchFlightDto());

        assertEquals(2, actualGetFlightDtoList.size());

        GetFlightDto expectedGetFlightDto1 = buildGetFlightDto();
        expectedGetFlightDto1.setId(id1);

        GetFlightDto expectedGetFlightDto2 = buildGetFlightDto();
        expectedGetFlightDto2.setId(id2);

        assertTrue(actualGetFlightDtoList.containsAll(List.of(expectedGetFlightDto1, expectedGetFlightDto2)));
    }
}
