package com.example.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "animal_visited_location")
public class AnimalVisitedLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "location_point", nullable = false)
    private LocationPoint locationPoint;

    @Temporal(TemporalType.DATE)
    @Column(name = "datetime_of_visit_location_point", nullable = false)
    private Date dateTimeOfVisitedLocationPoint;
}
