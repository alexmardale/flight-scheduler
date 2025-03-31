package com.example.flight_scheduler.utils;

import com.example.flight_scheduler.dto.CreateFlightDto;
import com.example.flight_scheduler.dto.GetFlightDto;
import com.example.flight_scheduler.dto.SearchFlightDto;
import com.example.flight_scheduler.model.Flight;
import com.example.flight_scheduler.model.FlightStatus;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class FlightUtils {
    private static final Long ID = 1L;
    private static final String FLIGHT_NUMBER = "flightNumber";
    private static final String DEPARTURE = "departure";
    private static final String DESTINATION = "destination";
    private static final OffsetDateTime DEPARTURE_TIME = OffsetDateTime.now().plusDays(1).truncatedTo(ChronoUnit.MICROS);
    private static final OffsetDateTime ARRIVAL_TIME = OffsetDateTime.now().plusDays(2).truncatedTo(ChronoUnit.MICROS);

    public static CreateFlightDto buildCreateFlightDto() {
        CreateFlightDto createFlightDto = new CreateFlightDto();
        createFlightDto.setFlightNumber(FLIGHT_NUMBER);
        createFlightDto.setDeparture(DEPARTURE);
        createFlightDto.setDestination(DESTINATION);
        createFlightDto.setDepartureTime(DEPARTURE_TIME);
        createFlightDto.setArrivalTime(ARRIVAL_TIME);

        return createFlightDto;
    }

    public static GetFlightDto buildGetFlightDto() {
        GetFlightDto getFlightDto = new GetFlightDto();
        getFlightDto.setId(ID);
        getFlightDto.setFlightNumber(FLIGHT_NUMBER);
        getFlightDto.setDeparture(DEPARTURE);
        getFlightDto.setDestination(DESTINATION);
        getFlightDto.setDepartureTime(DEPARTURE_TIME);
        getFlightDto.setArrivalTime(ARRIVAL_TIME);
        getFlightDto.setFlightStatus(FlightStatus.SCHEDULED);

        return getFlightDto;
    }

    public static Flight buildFlight(Long id) {
        Flight flight = new Flight();
        flight.setId(id);
        flight.setFlightNumber(FLIGHT_NUMBER);
        flight.setDeparture(DEPARTURE);
        flight.setDestination(DESTINATION);
        flight.setDepartureTime(DEPARTURE_TIME);
        flight.setArrivalTime(ARRIVAL_TIME);
        flight.setFlightStatus(FlightStatus.SCHEDULED);

        return flight;
    }

    public static SearchFlightDto buildSearchFlightDto() {
        SearchFlightDto searchFlightDto = new SearchFlightDto();
        searchFlightDto.setFlightNumber(FLIGHT_NUMBER);
        searchFlightDto.setDeparture(DEPARTURE);
        searchFlightDto.setDestination(DESTINATION);
        searchFlightDto.setFlightStatus(FlightStatus.SCHEDULED);

        return searchFlightDto;
    }

    public static Flight buildSearchFlightExample() {
        Flight flight = new Flight();
        flight.setFlightNumber(FLIGHT_NUMBER);
        flight.setDeparture(DEPARTURE);
        flight.setDestination(DESTINATION);
        flight.setFlightStatus(FlightStatus.SCHEDULED);

        return flight;
    }
}
