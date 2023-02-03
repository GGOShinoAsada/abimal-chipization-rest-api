package com.example.demo.repository;

import com.example.demo.model.AnimalType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalTypeRepository extends PagingAndSortingRepository<AnimalType, Long> {
}
