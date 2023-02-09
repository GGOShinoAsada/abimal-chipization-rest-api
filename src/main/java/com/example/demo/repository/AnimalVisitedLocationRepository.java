package com.example.demo.repository;

import com.example.demo.model.AnimalVisitedLocation;
import com.example.demo.model.LocationPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AnimalVisitedLocationRepository extends PagingAndSortingRepository<AnimalVisitedLocation, Long> {

    Optional<LocationPoint> findByLocationPoint_Id(Long id);

}
