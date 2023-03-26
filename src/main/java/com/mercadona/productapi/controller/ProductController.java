package com.mercadona.productapi.controller;

import com.mercadona.productapi.common.constants.UrlMvcConstants;
import com.mercadona.productapi.common.response.JSONResult;
import com.mercadona.productapi.exception.MapperException;
import com.mercadona.productapi.exception.ResourceNotFoundException;
import com.mercadona.productapi.model.Product;
import com.mercadona.productapi.services.ProductService;
import com.mercadona.productapi.validator.EAN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


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
     * @return the JSON with the status
     */
    @PostMapping(path = UrlMvcConstants.CREATE)
    public JSONResult<Boolean> createProduct(@RequestBody @Valid Product product) {
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
     * Updates a product by its EAN code.
     *
     * @param product the updated product
     * @return the JSON with the status of updateProduct
     */
    @PutMapping(path = UrlMvcConstants.UPDATE)
    public JSONResult<Product> updateProduct(@RequestBody @Valid Product product) {
        try {
            productService.updateProduct(product);
            return new JSONResult<>(true, "Product update successfully");
        } catch (ResourceNotFoundException e) {
            return new JSONResult<>(false, e.getMessage());
        } catch (Exception e) {
            return new JSONResult<>(false, "Error update product: " + e.getMessage());
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JSONResult<String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        return new JSONResult<>(false, "Validation error: " + String.join(", ", errors));
    }
}