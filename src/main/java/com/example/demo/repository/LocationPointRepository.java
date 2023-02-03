package com.example.demo.repository;

import com.example.demo.model.LocationPoint;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationPointRepository extends PagingAndSortingRepository<LocationPoint, Long> {
}
