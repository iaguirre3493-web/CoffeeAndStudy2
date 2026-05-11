package com.Accesos.CoffeeAndStudy.service;

import com.Accesos.CoffeeAndStudy.domain.Category;
import com.Accesos.CoffeeAndStudy.domain.Place;
import com.Accesos.CoffeeAndStudy.dto.PlaceRequestDto;
import com.Accesos.CoffeeAndStudy.dto.PlaceResponseDto;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.repository.CategoryRepository;
import com.Accesos.CoffeeAndStudy.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private PlaceService placeService;

    @Test
    void shouldReturnAllPlaces() {
        Category category = new Category();
        category.setName("Cafe");

        Place place = new Place();
        place.setTitle("Coffee Lab");
        place.setDescription("Lugar tranquilo");
        place.setLatitude(41.6488);
        place.setLongitude(-0.8891);
        place.setCity("Zaragoza");
        place.setWifiAvailable(true);
        place.setAveragePrice(2.5f);
        place.setOpeningDate(LocalDate.of(2025, 10, 1));
        place.setActive(true);
        place.setCategory(category);

        when(placeRepository.findAll()).thenReturn(List.of(place));

        List<PlaceResponseDto> result = placeService.filterPlaces(null, null, null);

        assertEquals(1, result.size());
        assertEquals("Coffee Lab", result.get(0).getTitle());
        assertEquals("Cafe", result.get(0).getCategoryName());
    }

    @Test
    void shouldReturnPlaceById() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Cafe");

        Place place = new Place();
        place.setTitle("Coffee Lab");
        place.setDescription("Lugar tranquilo");
        place.setLatitude(41.6488);
        place.setLongitude(-0.8891);
        place.setCity("Zaragoza");
        place.setWifiAvailable(true);
        place.setAveragePrice(2.5f);
        place.setOpeningDate(LocalDate.of(2025, 10, 1));
        place.setActive(true);
        place.setCategory(category);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(place));

        PlaceResponseDto result = placeService.getPlaceById(1L);

        assertEquals("Coffee Lab", result.getTitle());
        assertEquals(1L, result.getCategoryId());
    }

    @Test
    void shouldThrowExceptionWhenPlaceNotFound() {
        when(placeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> placeService.getPlaceById(99L));
    }

    @Test
    void shouldSavePlace() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Cafe");

        PlaceRequestDto dto = new PlaceRequestDto();
        dto.setTitle("Coffee Lab");
        dto.setDescription("Lugar tranquilo");
        dto.setLatitude(41.6488);
        dto.setLongitude(-0.8891);
        dto.setCity("Zaragoza");
        dto.setWifiAvailable(true);
        dto.setAveragePrice(2.5f);
        dto.setOpeningDate(LocalDate.of(2025, 10, 1));
        dto.setActive(true);
        dto.setCategoryId(1L);

        Place savedPlace = new Place();
        savedPlace.setTitle("Coffee Lab");
        savedPlace.setDescription("Lugar tranquilo");
        savedPlace.setLatitude(41.6488);
        savedPlace.setLongitude(-0.8891);
        savedPlace.setCity("Zaragoza");
        savedPlace.setWifiAvailable(true);
        savedPlace.setAveragePrice(2.5f);
        savedPlace.setOpeningDate(LocalDate.of(2025, 10, 1));
        savedPlace.setActive(true);
        savedPlace.setCategory(category);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(placeRepository.save(any(Place.class))).thenReturn(savedPlace);

        PlaceResponseDto result = placeService.savePlace(dto);

        assertEquals("Coffee Lab", result.getTitle());
        assertEquals("Cafe", result.getCategoryName());
        verify(placeRepository, times(1)).save(any(Place.class));
    }

    @Test
    void shouldUpdatePlace() {
        Category oldCategory = new Category();
        oldCategory.setId(1L);
        oldCategory.setName("Cafe");

        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setName("Library");

        Place existingPlace = new Place();
        existingPlace.setTitle("Old Place");
        existingPlace.setDescription("Old description");
        existingPlace.setLatitude(40.0);
        existingPlace.setLongitude(-1.0);
        existingPlace.setCity("Old City");
        existingPlace.setWifiAvailable(false);
        existingPlace.setAveragePrice(1.0f);
        existingPlace.setOpeningDate(LocalDate.of(2024, 1, 1));
        existingPlace.setActive(false);
        existingPlace.setCategory(oldCategory);

        PlaceRequestDto dto = new PlaceRequestDto();
        dto.setTitle("New Place");
        dto.setDescription("New description");
        dto.setLatitude(41.0);
        dto.setLongitude(-2.0);
        dto.setCity("New City");
        dto.setWifiAvailable(true);
        dto.setAveragePrice(3.0f);
        dto.setOpeningDate(LocalDate.of(2025, 1, 1));
        dto.setActive(true);
        dto.setCategoryId(2L);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(existingPlace));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));
        when(placeRepository.save(any(Place.class))).thenReturn(existingPlace);

        PlaceResponseDto result = placeService.updatePlace(1L, dto);

        assertEquals("New Place", result.getTitle());
        assertEquals("Library", result.getCategoryName());
    }

    @Test
    void shouldDeletePlace() {
        Place place = new Place();

        when(placeRepository.findById(1L)).thenReturn(Optional.of(place));

        placeService.deletePlace(1L);

        verify(placeRepository, times(1)).delete(place);
    }
}