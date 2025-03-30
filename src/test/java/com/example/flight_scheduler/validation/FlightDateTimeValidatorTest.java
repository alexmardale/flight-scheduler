package com.example.flight_scheduler.validation;

import com.example.flight_scheduler.model.Flight;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightDateTimeValidatorTest {
    private Validator validator;

    private Flight flight;

    @BeforeEach
    void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        flight = new Flight();
        flight.setFlightNumber("flightNumber");
        flight.setDeparture("departure");
        flight.setDestination("destination");
    }

    @Test
    void validFlightTest() {
        OffsetDateTime now = OffsetDateTime.now();
        flight.setDepartureTime(now.plusDays(1));
        flight.setArrivalTime(now.plusDays(2));

        Set<ConstraintViolation<Flight>> violations = validator.validate(flight);

        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidFlightTest() {
        OffsetDateTime now = OffsetDateTime.now();
        flight.setDepartureTime(now.plusDays(2));
        flight.setArrivalTime(now.plusDays(1));

        Set<ConstraintViolation<Flight>> violations = validator.validate(flight);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsExactly("Departure time must be before arrival time");
    }
}