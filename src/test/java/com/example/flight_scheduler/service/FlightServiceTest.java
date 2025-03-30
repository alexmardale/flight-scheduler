package com.example.flight_scheduler.service;

import com.example.flight_scheduler.dto.GetFlightDto;
import com.example.flight_scheduler.mapper.FlightMapper;
import com.example.flight_scheduler.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.flight_scheduler.utils.FlightUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {
    private static final Long ID = 1L;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightMapper flightMapper;

    @InjectMocks
    private FlightService flightService;

    @Test
    void createFlightTest() {
        when(flightMapper.toGetFlightDto(buildFlight(ID))).thenReturn(buildGetFlightDto());
        when(flightMapper.toEntity(buildCreateFlightDto())).thenReturn(buildFlight(null));
        when(flightRepository.save(buildFlight(null))).thenReturn(buildFlight(ID));

        GetFlightDto actualGetFlightDto = flightService.createFlight(buildCreateFlightDto());

        verify(flightRepository, times(1)).save(buildFlight(null));
        assertEquals(buildGetFlightDto(), actualGetFlightDto);
    }


}