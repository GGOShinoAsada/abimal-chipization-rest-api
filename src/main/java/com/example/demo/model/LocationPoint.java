package com.example.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "location_point")
public class LocationPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @OneToMany(mappedBy = "locationPoint")
    Set<AnimalVisitedLocation> locations;

    @OneToMany(mappedBy = "chippingLocationId")
    private Set<Animal> animals;
}
