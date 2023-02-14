package com.example.demo.repository;

import com.example.demo.model.Animal;
import com.example.demo.model.Gender;
import com.example.demo.model.LifeStatus;
import com.example.demo.model.LocationPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AnimalRepository extends PagingAndSortingRepository<Animal, Long> {

    Page<Animal> findByChippingDateTimeAfterAndChippingDateTimeBeforeAndChipperIdAndChippingLocationId_IdAndLifeStatusAndGender(Date startDate, Date endDate, Integer chipperId, Long chippingLocationId, LifeStatus lifeStatus, Gender gender, Pageable pageable);

    Optional<Animal> findByChippingLocationId_Id(Long id);

    Optional<Animal> findByChipperId(Integer id);
    
    
    
}
