package com.example.demo.repository;

import com.example.demo.model.LocationPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationPointRepository extends JpaRepository<LocationPoint, Long> {

    Optional<LocationPoint> findByLatitudeAndLongitude(Double latitude, Double longitude);

}
