package com.mercadona.productapi.entity;

import com.mercadona.productapi.validator.EAN;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "products")
@Data
public class ProductEntity {

    @Id
    @Column(name="ID")
    private int id;

    @EAN
    @Column(name="EAN")
    private String ean;

    @Column(name="NAME")
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="PRICE")
    private Double price;

    public ProductEntity(String ean, String name, String description, Double price){
        this.ean = ean;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public ProductEntity() {

    }
}
