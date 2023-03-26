package com.mercadona.productapi.services;

import com.mercadona.productapi.model.Product;
import java.util.List;

public interface ProductService {

    /**
     * Creates a new product in the system.
     *
     * @param product the product to create
     */
    void create(Product product);

    /**
     * Finds a product by its EAN code.
     *
     * @param ean the EAN code of the product to find
     * @return the found product or null if not found
     */
    Product findByEan(String ean);

    /**
     * Finds all products in the system.
     *
     * @return a list of all products
     */
    List<Product> findAll();

    /**
     * Update a product in the system.
     *
     * @param product the product to update
     */
    void updateProduct(Product product);

    /**
     * Deletes a product by its EAN code.
     *
     * @param ean the EAN code of the product to delete
     */
    void deleteByEan(String ean);

}
