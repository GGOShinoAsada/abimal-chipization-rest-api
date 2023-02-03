package com.example.demo.repository;

import com.example.demo.model.Animal;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends PagingAndSortingRepository<Animal, Long> {
}
