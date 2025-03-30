package com.example.flight_scheduler.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetFlightDto extends FlightDto implements Serializable {
    private Long id;
}
