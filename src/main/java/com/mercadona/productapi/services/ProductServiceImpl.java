package com.mercadona.productapi.services;

import com.mercadona.productapi.entity.ProductEntity;
import com.mercadona.productapi.exception.MapperException;
import com.mercadona.productapi.exception.ResourceNotFoundException;
import com.mercadona.productapi.mapper.ProductMapper;
import com.mercadona.productapi.model.Product;
import com.mercadona.productapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper, ProductRepository productRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }


    @Override
    @Transactional
    public void create(Product product) {
        ProductEntity productEntity = productMapper.productToEntity(product);
        ProductEntity productSearch = productRepository.findByEan(product.getEan());
        if (productSearch == null) {
            productRepository.save(productEntity);
        } else {
            throw new MapperException("Product not is mapped correctly");
        }
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "productsCache", key = "#ean")
    public Product findByEan(String ean) {
        ProductEntity productEntity = productRepository.findByEan(ean);
        if (productEntity != null) {
            return productMapper.entityToProduct(productRepository.findByEan(ean));
        } else {
            throw new ResourceNotFoundException("Product not found with EAN code: " + ean);
        }
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable("productsCache")
    public List<Product> findAll() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productMapper.entityToProductList(productEntities);
    }


    @Override
    @Transactional
    public void deleteByEan(String ean) {
        ProductEntity productEntity = productRepository.findByEan(ean);
        if (productEntity != null) {
            productRepository.delete(productEntity);
        } else {
            throw new ResourceNotFoundException("Product not found with EAN code: " + ean);
        }
    }
}
