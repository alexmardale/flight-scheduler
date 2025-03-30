package com.example.flight_scheduler.controller;

import com.example.flight_scheduler.configuration.AppConfig;
import com.example.flight_scheduler.dto.GetFlightDto;
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

import static com.example.flight_scheduler.utils.FlightUtils.buildCreateFlightDto;
import static com.example.flight_scheduler.utils.FlightUtils.buildGetFlightDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class)
@Import(AppConfig.class)
class FlightControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightService flightService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createFlightTest() throws Exception {
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
}