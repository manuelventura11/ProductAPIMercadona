package com.mercadona.productapi.controller;

import com.mercadona.productapi.common.constants.UrlMvcConstants;
import com.mercadona.productapi.common.response.JSONResult;
import com.mercadona.productapi.exception.MapperException;
import com.mercadona.productapi.exception.ResourceNotFoundException;
import com.mercadona.productapi.model.Product;
import com.mercadona.productapi.services.ProductService;
import com.mercadona.productapi.validator.EAN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = UrlMvcConstants.PRODUCTS, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product.
     *
     * @param product the product to create
     * @return the created product
     */
    @PostMapping(path = UrlMvcConstants.CREATE)
    public JSONResult<Boolean> createProduct(@RequestBody @Valid @EAN Product product) {
        try {
            productService.create(product);
            return new JSONResult<>(true, "Product created successfully");
        } catch (MapperException e) {
            return new JSONResult<>(false, e.getMessage());
        } catch (Exception e) {
            return new JSONResult<>(false, "Error creating product: " + e.getMessage());
        }
    }

    /**
     * Retrieves a product by its EAN code.
     *
     * @param ean the EAN code of the product to retrieve
     * @return the product corresponding to the EAN code
     */
    @GetMapping(path = UrlMvcConstants.EAN)
    public JSONResult<Product> getProductByEan(@PathVariable @Valid @EAN String ean) {
        try {
            return new JSONResult<>(productService.findByEan(ean));
        } catch (ResourceNotFoundException e) {
            return new JSONResult<>(false, e.getMessage());
        }
    }

    /**
     * Retrieves all products.
     *
     * @return a list of all products
     */
    @GetMapping
    public JSONResult<List<Product>> getAllProducts() {
        return new JSONResult<>(productService.findAll());
    }


    /**
     * Deletes a product by its EAN code.
     *
     * @param ean the EAN code of the product to delete
     */
    @DeleteMapping(path = UrlMvcConstants.DELETE)
    public JSONResult<Boolean> deleteProduct(@PathVariable @EAN String ean) {
        try {
            productService.deleteByEan(ean);
            return new JSONResult<>(true, "Product deleted successfully");
        } catch (ResourceNotFoundException e) {
            return new JSONResult<>(false, e.getMessage());
        }
    }
}