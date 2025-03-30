package com.example.flight_scheduler.validation;

import com.example.flight_scheduler.model.Flight;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FlightDateTimeValidator implements ConstraintValidator<FlightDateTimeConstraint, Flight> {
    @Override
    public boolean isValid(Flight flight, ConstraintValidatorContext context) {
        return flight.getDepartureTime().isBefore(flight.getArrivalTime());
    }
}
