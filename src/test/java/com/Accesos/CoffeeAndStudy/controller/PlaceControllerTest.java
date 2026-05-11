package com.Accesos.CoffeeAndStudy.controller;

import com.Accesos.CoffeeAndStudy.dto.PlaceRequestDto;
import com.Accesos.CoffeeAndStudy.dto.PlaceResponseDto;
import com.Accesos.CoffeeAndStudy.exception.GlobalExceptionHandler;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.service.PlaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PlaceControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PlaceService placeService;

    @InjectMocks
    private PlaceController placeController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(placeController, "placeService", placeService);

        mockMvc = MockMvcBuilders.standaloneSetup(placeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void shouldReturnAllPlaces() throws Exception {
        PlaceResponseDto dto = new PlaceResponseDto();
        dto.setId(1L);
        dto.setTitle("Coffee Lab");
        dto.setCategoryId(1L);
        dto.setCategoryName("Cafe");
        dto.setDescription("Lugar tranquilo");
        dto.setLatitude(41.6488);
        dto.setLongitude(-0.8891);
        dto.setCity("Zaragoza");
        dto.setWifiAvailable(true);
        dto.setAveragePrice(2.5f);
        dto.setOpeningDate(LocalDate.of(2025, 10, 1));
        dto.setActive(true);

        when(placeService.filterPlaces(null, null, null)).thenReturn(List.of(dto));

        mockMvc.perform(get("/places"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Coffee Lab"))
                .andExpect(jsonPath("$[0].categoryName").value("Cafe"));
    }

    @Test
    void shouldReturnPlaceById() throws Exception {
        PlaceResponseDto dto = new PlaceResponseDto();
        dto.setId(1L);
        dto.setTitle("Coffee Lab");
        dto.setCategoryId(1L);
        dto.setCategoryName("Cafe");

        when(placeService.getPlaceById(1L)).thenReturn(dto);

        mockMvc.perform(get("/places/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Coffee Lab"))
                .andExpect(jsonPath("$.categoryId").value(1));
    }

    @Test
    void shouldReturn404WhenPlaceNotFound() throws Exception {
        when(placeService.getPlaceById(99L))
                .thenThrow(new ResourceNotFoundException("Place with id 99 not found"));

        mockMvc.perform(get("/places/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreatePlace() throws Exception {
        PlaceRequestDto requestDto = new PlaceRequestDto();
        requestDto.setTitle("Coffee Lab");
        requestDto.setDescription("Lugar tranquilo");
        requestDto.setLatitude(41.6488);
        requestDto.setLongitude(-0.8891);
        requestDto.setCity("Zaragoza");
        requestDto.setWifiAvailable(true);
        requestDto.setAveragePrice(2.5f);
        requestDto.setOpeningDate(LocalDate.of(2025, 10, 1));
        requestDto.setActive(true);
        requestDto.setCategoryId(1L);

        PlaceResponseDto responseDto = new PlaceResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Coffee Lab");
        responseDto.setCategoryId(1L);
        responseDto.setCategoryName("Cafe");

        when(placeService.savePlace(any(PlaceRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/places")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Coffee Lab"))
                .andExpect(jsonPath("$.categoryName").value("Cafe"));
    }

    @Test
    void shouldUpdatePlace() throws Exception {
        PlaceRequestDto requestDto = new PlaceRequestDto();
        requestDto.setTitle("Coffee Lab Updated");
        requestDto.setDescription("Nuevo texto");
        requestDto.setLatitude(41.0);
        requestDto.setLongitude(-1.0);
        requestDto.setCity("Zaragoza");
        requestDto.setWifiAvailable(true);
        requestDto.setAveragePrice(3.0f);
        requestDto.setOpeningDate(LocalDate.of(2025, 10, 2));
        requestDto.setActive(true);
        requestDto.setCategoryId(1L);

        PlaceResponseDto responseDto = new PlaceResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Coffee Lab Updated");
        responseDto.setCategoryId(1L);
        responseDto.setCategoryName("Cafe");

        when(placeService.updatePlace(eq(1L), any(PlaceRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/places/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Coffee Lab Updated"));
    }

    @Test
    void shouldDeletePlace() throws Exception {
        doNothing().when(placeService).deletePlace(1L);

        mockMvc.perform(delete("/places/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn400WhenCreatingInvalidPlace() throws Exception {
        PlaceRequestDto invalidRequestDto = new PlaceRequestDto();
        invalidRequestDto.setTitle("");
        invalidRequestDto.setDescription("Lugar tranquilo");
        invalidRequestDto.setLatitude(200.0);
        invalidRequestDto.setLongitude(-0.8891);
        invalidRequestDto.setCity("Zaragoza");
        invalidRequestDto.setWifiAvailable(true);
        invalidRequestDto.setAveragePrice(2.5f);
        invalidRequestDto.setOpeningDate(LocalDate.of(2025, 10, 1));
        invalidRequestDto.setActive(true);
        invalidRequestDto.setCategoryId(1L);

        mockMvc.perform(post("/places")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }
}