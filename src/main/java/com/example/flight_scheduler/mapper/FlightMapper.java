package com.example.flight_scheduler.mapper;

import com.example.flight_scheduler.dto.CreateFlightDto;
import com.example.flight_scheduler.dto.GetFlightDto;
import com.example.flight_scheduler.dto.SearchFlightDto;
import com.example.flight_scheduler.model.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.ERROR
)
public interface FlightMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flightStatus", expression = "java(com.example.flight_scheduler.model.FlightStatus.SCHEDULED)")
    Flight toEntity(CreateFlightDto createFlightDto);

    GetFlightDto toGetFlightDto(Flight flight);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departureTime", ignore = true)
    @Mapping(target = "arrivalTime", ignore = true)
    Flight toEntity(SearchFlightDto searchFlightDto);
}
