package com.Accesos.CoffeeAndStudy.repository;

import com.Accesos.CoffeeAndStudy.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByCityContainingIgnoreCaseAndWifiAvailableAndActive(String city, Boolean wifiAvailable, Boolean active);

    List<Place> findByCityContainingIgnoreCase(String city);

    List<Place> findByWifiAvailable(Boolean wifiAvailable);

    List<Place> findByActive(Boolean active);
}