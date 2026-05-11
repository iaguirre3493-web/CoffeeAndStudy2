package com.Accesos.CoffeeAndStudy.service;

import com.Accesos.CoffeeAndStudy.domain.Category;
import com.Accesos.CoffeeAndStudy.domain.Place;
import com.Accesos.CoffeeAndStudy.dto.PlaceRequestDto;
import com.Accesos.CoffeeAndStudy.dto.PlaceResponseDto;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.repository.CategoryRepository;
import com.Accesos.CoffeeAndStudy.repository.PlaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {

    private static final Logger logger = LoggerFactory.getLogger(PlaceService.class);

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<PlaceResponseDto> filterPlaces(String city, Boolean wifiAvailable, Boolean active) {
        logger.info("Filtering places with city={}, wifiAvailable={}, active={}", city, wifiAvailable, active);

        List<Place> places;

        if (city != null && wifiAvailable != null && active != null) {
            places = placeRepository.findByCityContainingIgnoreCaseAndWifiAvailableAndActive(city, wifiAvailable, active);
        } else if (city != null) {
            places = placeRepository.findByCityContainingIgnoreCase(city);
        } else if (wifiAvailable != null) {
            places = placeRepository.findByWifiAvailable(wifiAvailable);
        } else if (active != null) {
            places = placeRepository.findByActive(active);
        } else {
            places = placeRepository.findAll();
        }

        return places.stream().map(this::toResponseDto).toList();
    }

    public PlaceResponseDto getPlaceById(Long id) {
        logger.info("Getting place by id={}", id);

        Place place = placeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Place with id {} not found", id);
                    return new ResourceNotFoundException("Place with id " + id + " not found");
                });

        return toResponseDto(place);
    }

    public PlaceResponseDto savePlace(PlaceRequestDto placeDto) {
        logger.info("Saving new place with title={}", placeDto.getTitle());

        Category category = categoryRepository.findById(placeDto.getCategoryId())
                .orElseThrow(() -> {
                    logger.error("Category with id {} not found for place creation", placeDto.getCategoryId());
                    return new ResourceNotFoundException("Category with id " + placeDto.getCategoryId() + " not found");
                });

        Place place = new Place();
        place.setTitle(placeDto.getTitle());
        place.setDescription(placeDto.getDescription());
        place.setLatitude(placeDto.getLatitude());
        place.setLongitude(placeDto.getLongitude());
        place.setCity(placeDto.getCity());
        place.setWifiAvailable(placeDto.getWifiAvailable());
        place.setAveragePrice(placeDto.getAveragePrice());
        place.setOpeningDate(placeDto.getOpeningDate());
        place.setActive(placeDto.getActive());
        place.setCategory(category);

        Place savedPlace = placeRepository.save(place);
        return toResponseDto(savedPlace);
    }

    public PlaceResponseDto updatePlace(Long id, PlaceRequestDto placeDto) {
        logger.info("Updating place with id={}", id);

        Place place = placeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Place with id {} not found for update", id);
                    return new ResourceNotFoundException("Place with id " + id + " not found");
                });

        Category category = categoryRepository.findById(placeDto.getCategoryId())
                .orElseThrow(() -> {
                    logger.error("Category with id {} not found for place update", placeDto.getCategoryId());
                    return new ResourceNotFoundException("Category with id " + placeDto.getCategoryId() + " not found");
                });

        place.setTitle(placeDto.getTitle());
        place.setDescription(placeDto.getDescription());
        place.setLatitude(placeDto.getLatitude());
        place.setLongitude(placeDto.getLongitude());
        place.setCity(placeDto.getCity());
        place.setWifiAvailable(placeDto.getWifiAvailable());
        place.setAveragePrice(placeDto.getAveragePrice());
        place.setOpeningDate(placeDto.getOpeningDate());
        place.setActive(placeDto.getActive());
        place.setCategory(category);

        logger.info("Place with id {} updated successfully", id);
        Place updatedPlace = placeRepository.save(place);
        return toResponseDto(updatedPlace);
    }

    public void deletePlace(Long id) {
        logger.info("Deleting place with id={}", id);

        Place place = placeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Place with id {} not found for delete", id);
                    return new ResourceNotFoundException("Place with id " + id + " not found");
                });

        placeRepository.delete(place);
        logger.info("Place with id {} deleted successfully", id);
    }

    private PlaceResponseDto toResponseDto(Place place) {
        PlaceResponseDto dto = new PlaceResponseDto();
        dto.setId(place.getId());
        dto.setTitle(place.getTitle());
        dto.setDescription(place.getDescription());
        dto.setLatitude(place.getLatitude());
        dto.setLongitude(place.getLongitude());
        dto.setCity(place.getCity());
        dto.setWifiAvailable(place.getWifiAvailable());
        dto.setAveragePrice(place.getAveragePrice());
        dto.setOpeningDate(place.getOpeningDate());
        dto.setActive(place.getActive());
        dto.setCategoryId(place.getCategory().getId());
        dto.setCategoryName(place.getCategory().getName());
        return dto;
    }
}