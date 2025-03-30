package com.example.flight_scheduler.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FlightDateTimeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlightDateTimeConstraint {
    String message() default "Departure time must be before arrival time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
