package com.mercadona.productapi.model;

import com.mercadona.productapi.validator.EAN;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "Product")
public class Product {
    @EAN
    private String ean;
    private String name;
    private String description;
    private double price;
    private String supplier;
    private String destination;

    public Product(String ean, String name, String description, double price){
        this.ean = ean;
        this.name = name;
        this.description = description;
        this.price = price;
        this.supplier = ean.substring(0, 7);
        this.destination = getDestination();
    }

    public String getDestination() {
        char lastDigit = ean.charAt(ean.length() - 1);
        switch (lastDigit) {
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
                return "Mercadona España";
            case '6':
                return "Mercadona Portugal";
            case '8':
                return "Almacenes";
            case '9':
                return "Oficinas Mercadona";
            case '0':
                return "Colmenas";
            default:
                return "Destino desconocido";
        }
    }
}
