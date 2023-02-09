package com.example.demo.repository;

import com.example.demo.model.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimalTypeRepository extends JpaRepository<AnimalType, Long> {

    Optional<AnimalType> findByType(String type);
}
