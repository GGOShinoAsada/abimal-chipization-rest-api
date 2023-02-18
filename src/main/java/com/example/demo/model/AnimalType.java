package com.example.demo.model;

import lombok.Data;
import javax.persistence.*;

/**
 * сущность AnimalType
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Data
@Entity
@Table(name = "animal_type")
public class AnimalType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;
}
