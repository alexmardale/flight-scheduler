package com.example.flight_scheduler.validation;

import com.example.flight_scheduler.dto.FlightDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FlightDtoDateTimeValidator implements ConstraintValidator<FlightDateTimeConstraint, FlightDto> {
    @Override
    public boolean isValid(FlightDto flightDto, ConstraintValidatorContext context) {
        return flightDto.getDepartureTime().isBefore(flightDto.getArrivalTime());
    }
}
