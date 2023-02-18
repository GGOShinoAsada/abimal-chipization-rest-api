package com.example.demo.model;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * сущность AnimalVisitedLocation
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Data
@Entity
@Table(name = "animal_visited_location")
public class AnimalVisitedLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "location_point", nullable = true)
    private LocationPoint locationPoint;

    @Temporal(TemporalType.DATE)
    @Column(name = "datetime_of_visit_location_point", nullable = false)
    private Date dateTimeOfVisitLocationPoint;
}
