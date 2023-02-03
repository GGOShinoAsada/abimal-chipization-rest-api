package com.example.demo.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "animal_type")
public class AnimalType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", length = 50, nullable = false)
    private String type;
}
