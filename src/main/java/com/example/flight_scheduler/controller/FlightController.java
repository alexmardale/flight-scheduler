package com.example.flight_scheduler.controller;

import com.example.flight_scheduler.dto.CreateFlightDto;
import com.example.flight_scheduler.dto.GetFlightDto;
import com.example.flight_scheduler.dto.SearchFlightDto;
import com.example.flight_scheduler.exception.FlightNotFoundException;
import com.example.flight_scheduler.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/flights")
@Validated
@Slf4j
public class FlightController {
    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Operation(summary = "Create a new flight")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully created a new flight",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetFlightDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Validation issue")
    })
    @PostMapping
    ResponseEntity<GetFlightDto> createFlight(@RequestBody @Valid CreateFlightDto createFlightDto) {
        log.info("Received request to create flight: {}", createFlightDto);
        return new ResponseEntity<>(flightService.createFlight(createFlightDto), HttpStatus.OK);
    }

    @Operation(summary = "Retrieve a flight by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved specified flight",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetFlightDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    @GetMapping(path = "/{id}")
    ResponseEntity<GetFlightDto> getFlight(@PathVariable("id") @Parameter(name = "id", description = "Id of flight to retrieve") Long id) {
        log.info("Received request to retrieve flight by id: {}", id);
        try {
            return new ResponseEntity<>(flightService.getFlight(id), HttpStatus.OK);
        } catch (FlightNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Retrieve flights")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved flights")
    })
    @GetMapping
    ResponseEntity<List<GetFlightDto>> getFlights() {
        log.info("Received request to retrieve flights");
        return new ResponseEntity<>(flightService.getFlights(), HttpStatus.OK);
    }

    @Operation(summary = "Cancel a flight by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully cancelled specified flight",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetFlightDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    @PutMapping(path = "/{id}")
    ResponseEntity<GetFlightDto> cancelFlight(@PathVariable("id") @Parameter(name = "id", description = "Id of flight to cancel") Long id) {
        log.info("Received request to cancel flight by id: {}", id);
        try {
            return new ResponseEntity<>(flightService.cancelFlight(id), HttpStatus.OK);
        } catch (FlightNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Search flights")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved flights")
    })
    @PostMapping(path = "/search")
    ResponseEntity<List<GetFlightDto>> searchFlights(@RequestBody @Valid SearchFlightDto searchFlightDto) {
        log.info("Received request to search flights");
        return new ResponseEntity<>(flightService.searchFlights(searchFlightDto), HttpStatus.OK);
    }
}
