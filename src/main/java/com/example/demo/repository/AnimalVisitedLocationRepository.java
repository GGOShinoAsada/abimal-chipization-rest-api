package com.example.demo.repository;

import com.example.demo.model.AnimalVisitedLocation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalVisitedLocationRepository extends PagingAndSortingRepository<AnimalVisitedLocation, Long> {
}
