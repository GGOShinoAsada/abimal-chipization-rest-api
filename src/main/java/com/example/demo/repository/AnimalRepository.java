package com.example.demo.repository;

import com.example.demo.model.Animal;
import com.example.demo.model.Gender;
import com.example.demo.model.LifeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

/**
 * репозиторий для сущности Animal
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Repository
public interface AnimalRepository extends PagingAndSortingRepository<Animal, Long> {


    Page<Animal> findByChippingDateTimeAfterAndChippingDateTimeBeforeAndChipperIdAndChippingLocationId_IdAndLifeStatusAndGender(Date startDate, Date endDate, Integer chipperId, Long chippingLocationId, LifeStatus lifeStatus, Gender gender, Pageable pageable);

    Optional<Animal> findByChippingLocationId_Id(Long id);

    Optional<Animal> findByChipperId(Integer id);
    
    
    
}
