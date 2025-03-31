package com.example.flight_scheduler.controller;

import com.example.flight_scheduler.configuration.AppConfig;
import com.example.flight_scheduler.dto.CreateFlightDto;
import com.example.flight_scheduler.dto.GetFlightDto;
import com.example.flight_scheduler.exception.FlightNotFoundException;
import com.example.flight_scheduler.model.FlightStatus;
import com.example.flight_scheduler.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.example.flight_scheduler.utils.FlightUtils.buildCreateFlightDto;
import static com.example.flight_scheduler.utils.FlightUtils.buildGetFlightDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class)
@Import(AppConfig.class)
class FlightControllerTest {
    private static final Long ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightService flightService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createFlightNominalTest() throws Exception {
        when(flightService.createFlight(any())).thenReturn(buildGetFlightDto());

        MvcResult mvcResult = mockMvc
                .perform(
                        post("/flights")
                                .content(objectMapper.writeValueAsString((buildCreateFlightDto())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        GetFlightDto actualGetFlightDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GetFlightDto.class);
        assertEquals(buildGetFlightDto(), actualGetFlightDto);
    }

    @Test
    void createFlightValidationIssueTest() throws Exception {
        CreateFlightDto createFlightDto = buildCreateFlightDto();
        createFlightDto.setDeparture(null);

        mockMvc
                .perform(
                        post("/flights")
                                .content(objectMapper.writeValueAsString((createFlightDto)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFlightNominalTest() throws Exception {
        when(flightService.getFlight(ID)).thenReturn(buildGetFlightDto());

        MvcResult mvcResult = mockMvc
                .perform(
                        get("/flights/" + ID)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        GetFlightDto actualGetFlightDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GetFlightDto.class);
        assertEquals(buildGetFlightDto(), actualGetFlightDto);
    }

    @Test
    void getFlightNotFoundTest() throws Exception {
        when(flightService.getFlight(ID)).thenThrow(FlightNotFoundException.class);

        mockMvc
                .perform(
                        get("/flights/" + ID)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getFlightsMultipleFlightsTest() throws Exception {
        GetFlightDto getFlightDto1 = buildGetFlightDto();
        getFlightDto1.setId(1L);

        GetFlightDto getFlightDto2 = buildGetFlightDto();
        getFlightDto2.setId(2L);

        GetFlightDto getFlightDto3 = buildGetFlightDto();
        getFlightDto3.setId(3L);

        when(flightService.getFlights()).thenReturn(List.of(getFlightDto1, getFlightDto2, getFlightDto3));

        MvcResult mvcResult = mockMvc
                .perform(
                        get("/flights")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<GetFlightDto> actualGetFlightDtoList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, GetFlightDto.class));

        GetFlightDto expectedGetFlightDto1 = buildGetFlightDto();
        expectedGetFlightDto1.setId(1L);

        GetFlightDto expectedGetFlightDto2 = buildGetFlightDto();
        expectedGetFlightDto2.setId(2L);

        GetFlightDto expectedGetFlightDto3 = buildGetFlightDto();
        expectedGetFlightDto3.setId(3L);

        assertTrue(actualGetFlightDtoList.containsAll(List.of(expectedGetFlightDto1, expectedGetFlightDto2, expectedGetFlightDto3)));
    }

    @Test
    void cancelFlightNominalTest() throws Exception {
        GetFlightDto getFlightDto = buildGetFlightDto();
        getFlightDto.setFlightStatus(FlightStatus.CANCELLED);
        when(flightService.cancelFlight(ID)).thenReturn(getFlightDto);

        MvcResult mvcResult = mockMvc
                .perform(
                        put("/flights/" + ID)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        GetFlightDto expectedGetFlightDto = buildGetFlightDto();
        expectedGetFlightDto.setFlightStatus(FlightStatus.CANCELLED);
        GetFlightDto actualGetFlightDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GetFlightDto.class);
        assertEquals(expectedGetFlightDto, actualGetFlightDto);
    }

    @Test
    void cancelFlightNotFoundTest() throws Exception {
        when(flightService.cancelFlight(ID)).thenThrow(FlightNotFoundException.class);

        mockMvc
                .perform(
                        put("/flights/" + ID)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}