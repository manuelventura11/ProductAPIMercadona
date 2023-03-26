package com.mercadona.productapi.mapper;

import com.mercadona.productapi.entity.ProductEntity;
import com.mercadona.productapi.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapperImpl implements ProductMapper {
    @Override
    public Product entityToProduct(ProductEntity entity) {
        Product product = new Product(entity.getEan(), entity.getName(), entity.getDescription(), entity.getPrice());
        return product;
    }

    public List<Product> entityToProductList(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(this::entityToProduct)
                .collect(Collectors.toList());
    }

    @Override
    public ProductEntity productToEntity(Product product) {
        ProductEntity productEntity = new ProductEntity(product.getEan(), product.getName(), product.getDescription(), product.getPrice());
        return productEntity;
    }
}