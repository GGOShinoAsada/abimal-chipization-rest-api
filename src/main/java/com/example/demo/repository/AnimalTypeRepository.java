package com.example.demo.repository;

import com.example.demo.model.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * репозиторий для сущности AnimalType
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Repository
public interface AnimalTypeRepository extends JpaRepository<AnimalType, Long> {

    Optional<AnimalType> findByType(String type);
}
