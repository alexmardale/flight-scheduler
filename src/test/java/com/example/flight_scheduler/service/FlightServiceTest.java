package com.example.flight_scheduler.service;

import com.example.flight_scheduler.dto.GetFlightDto;
import com.example.flight_scheduler.exception.FlightNotFoundException;
import com.example.flight_scheduler.mapper.FlightMapper;
import com.example.flight_scheduler.model.Flight;
import com.example.flight_scheduler.model.FlightStatus;
import com.example.flight_scheduler.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

import static com.example.flight_scheduler.utils.FlightUtils.*;
import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void getFlightNominalTest() throws FlightNotFoundException {
        when(flightMapper.toGetFlightDto(buildFlight(ID))).thenReturn(buildGetFlightDto());
        when(flightRepository.findById(ID)).thenReturn(Optional.of(buildFlight(ID)));

        GetFlightDto actualGetFlightDto = flightService.getFlight(ID);

        assertEquals(buildGetFlightDto(), actualGetFlightDto);
    }

    @Test
    void getFlightNotFoundTest() {
        when(flightRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(
                FlightNotFoundException.class,
                () -> flightService.getFlight(ID));
    }

    @Test
    void getFlightsEmptyTest() {
        when(flightRepository.findAll()).thenReturn(List.of());

        List<GetFlightDto> getFlightDtoList = flightService.getFlights();

        assertTrue(getFlightDtoList.isEmpty());
    }

    @Test
    void getFlightsOneFlightTest() {
        when(flightMapper.toGetFlightDto(buildFlight(ID))).thenReturn(buildGetFlightDto());
        when(flightRepository.findAll()).thenReturn(List.of(buildFlight(ID)));

        List<GetFlightDto> actualGetFlightDtoList = flightService.getFlights();

        assertEquals(1, actualGetFlightDtoList.size());
        assertEquals(buildGetFlightDto(), actualGetFlightDtoList.getFirst());
    }

    @Test
    void getFlightsMultipleFlightsTest() {
        GetFlightDto getFlightDto1 = buildGetFlightDto();
        getFlightDto1.setId(1L);

        GetFlightDto getFlightDto2 = buildGetFlightDto();
        getFlightDto2.setId(2L);

        GetFlightDto getFlightDto3 = buildGetFlightDto();
        getFlightDto3.setId(3L);

        when(flightMapper.toGetFlightDto(buildFlight(1L))).thenReturn(getFlightDto1);
        when(flightMapper.toGetFlightDto(buildFlight(2L))).thenReturn(getFlightDto2);
        when(flightMapper.toGetFlightDto(buildFlight(3L))).thenReturn(getFlightDto3);
        when(flightRepository.findAll()).thenReturn(List.of(buildFlight(1L), buildFlight(2L), buildFlight(3L)));

        List<GetFlightDto> actualGetFlightDtoList = flightService.getFlights();

        assertEquals(3, actualGetFlightDtoList.size());

        GetFlightDto expectedGetFlightDto1 = buildGetFlightDto();
        expectedGetFlightDto1.setId(1L);

        GetFlightDto expectedGetFlightDto2 = buildGetFlightDto();
        expectedGetFlightDto2.setId(2L);

        GetFlightDto expectedGetFlightDto3 = buildGetFlightDto();
        expectedGetFlightDto3.setId(3L);

        assertTrue(actualGetFlightDtoList.containsAll(List.of(expectedGetFlightDto1, expectedGetFlightDto2, expectedGetFlightDto3)));
    }

    @Test
    void cancelFlightNominalTest() throws FlightNotFoundException {
        GetFlightDto getFlightDto = buildGetFlightDto();
        getFlightDto.setFlightStatus(FlightStatus.CANCELLED);
        Flight flight = buildFlight(ID);
        flight.setFlightStatus(FlightStatus.CANCELLED);
        when(flightMapper.toGetFlightDto(flight)).thenReturn(getFlightDto);
        when(flightRepository.findById(ID)).thenReturn(Optional.of(buildFlight(ID)));

        GetFlightDto actualGetFlightDto = flightService.cancelFlight(ID);

        Flight expectedCancelledFlight = buildFlight(ID);
        expectedCancelledFlight.setFlightStatus(FlightStatus.CANCELLED);
        verify(flightRepository, times(1)).save(expectedCancelledFlight);

        GetFlightDto expectedGetFlightDto = buildGetFlightDto();
        expectedGetFlightDto.setFlightStatus(FlightStatus.CANCELLED);
        assertEquals(expectedGetFlightDto, actualGetFlightDto);
    }

    @Test
    void cancelFlightNotFoundTest() {
        when(flightRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(
                FlightNotFoundException.class,
                () -> flightService.cancelFlight(ID));
    }

    @Test
    void searchFlightsEmptyTest() {
        when(flightMapper.toEntity(buildSearchFlightDto())).thenReturn(buildSearchFlightExample());
        when(flightRepository.findAll(Example.of(buildSearchFlightExample()))).thenReturn(List.of());

        List<GetFlightDto> getFlightDtoList = flightService.searchFlights(buildSearchFlightDto());

        assertTrue(getFlightDtoList.isEmpty());
        verify(flightRepository, times(1)).findAll(Example.of(buildSearchFlightExample()));
    }

    @Test
    void searchFlightsOneFlightTest() {
        when(flightMapper.toEntity(buildSearchFlightDto())).thenReturn(buildSearchFlightExample());
        when(flightMapper.toGetFlightDto(buildFlight(ID))).thenReturn(buildGetFlightDto());
        when(flightRepository.findAll(Example.of(buildSearchFlightExample()))).thenReturn(List.of(buildFlight(ID)));

        List<GetFlightDto> actualGetFlightDtoList = flightService.searchFlights(buildSearchFlightDto());

        assertEquals(1, actualGetFlightDtoList.size());
        assertEquals(buildGetFlightDto(), actualGetFlightDtoList.getFirst());
        verify(flightRepository, times(1)).findAll(Example.of(buildSearchFlightExample()));
    }

    @Test
    void searchFlightsMultipleFlightsTest() {
        when(flightMapper.toEntity(buildSearchFlightDto())).thenReturn(buildSearchFlightExample());

        GetFlightDto getFlightDto1 = buildGetFlightDto();
        getFlightDto1.setId(1L);

        GetFlightDto getFlightDto2 = buildGetFlightDto();
        getFlightDto2.setId(2L);

        GetFlightDto getFlightDto3 = buildGetFlightDto();
        getFlightDto3.setId(3L);

        when(flightMapper.toGetFlightDto(buildFlight(1L))).thenReturn(getFlightDto1);
        when(flightMapper.toGetFlightDto(buildFlight(2L))).thenReturn(getFlightDto2);
        when(flightMapper.toGetFlightDto(buildFlight(3L))).thenReturn(getFlightDto3);
        when(flightRepository.findAll(Example.of(buildSearchFlightExample()))).thenReturn(List.of(buildFlight(1L), buildFlight(2L), buildFlight(3L)));

        List<GetFlightDto> actualGetFlightDtoList = flightService.searchFlights(buildSearchFlightDto());

        assertEquals(3, actualGetFlightDtoList.size());

        GetFlightDto expectedGetFlightDto1 = buildGetFlightDto();
        expectedGetFlightDto1.setId(1L);

        GetFlightDto expectedGetFlightDto2 = buildGetFlightDto();
        expectedGetFlightDto2.setId(2L);

        GetFlightDto expectedGetFlightDto3 = buildGetFlightDto();
        expectedGetFlightDto3.setId(3L);

        assertTrue(actualGetFlightDtoList.containsAll(List.of(expectedGetFlightDto1, expectedGetFlightDto2, expectedGetFlightDto3)));

        verify(flightRepository, times(1)).findAll(Example.of(buildSearchFlightExample()));
    }
}