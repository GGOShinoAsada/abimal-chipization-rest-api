package com.example.demo.repository;

import com.example.demo.model.AnimalVisitedLocation;
import com.example.demo.model.LocationPoint;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * репозиторий для сущности AnimalVisitedLocations
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Repository
public interface AnimalVisitedLocationRepository extends PagingAndSortingRepository<AnimalVisitedLocation, Long> {

    Optional<LocationPoint> findByLocationPoint_Id(Long id);

}
