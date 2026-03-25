package com.uade.tpo.marketplace.entity;

import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@Entity
public class Category {

    public Category() {
    }

    public Category(String description) {
        this.description = description;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false, unique = true)
    private String description;

}

