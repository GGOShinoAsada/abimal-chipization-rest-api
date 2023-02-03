package com.example.demo.model;

import com.example.demo.config.AnimalTypeListConverter;
import com.example.demo.config.LocationPointListConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "animal")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "animal_types", nullable = false)
    @Convert(converter = AnimalTypeListConverter.class)
    private Set<AnimalType> animalTypes;

    @Column(name = "weight", nullable = false)
    private Float weight;

    @Column(name = "height", nullable = false)
    private Float height;

    @Column(name = "length", nullable = false)
    private Float length;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "life_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LifeStatus lifeStatus;

    @Column(name = "chipping_datetime", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date chippingDatetime;

    @Column(name = "chipper_id",nullable = false)
    private  Integer chippedId;

    @ManyToOne
    @JoinColumn(name = "chipping_location_id", nullable = false)
    private LocationPoint chippingLocationId;

    @Column(name = "visited_locations",nullable = false)
    @Convert(converter = LocationPointListConverter.class)
    private Set<LocationPoint> visitedLocations;

    @Column(name = "death_datetime", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date deathDatetime;
}
