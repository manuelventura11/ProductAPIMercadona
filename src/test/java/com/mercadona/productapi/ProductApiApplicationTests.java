package com.mercadona.productapi;

import com.mercadona.productapi.common.constants.UrlMvcConstants;
import com.mercadona.productapi.common.response.JSONResult;
import com.mercadona.productapi.controller.ProductController;
import com.mercadona.productapi.entity.ProductEntity;
import com.mercadona.productapi.mapper.ProductMapper;
import com.mercadona.productapi.mapper.ProductMapperImpl;
import com.mercadona.productapi.model.Product;
import com.mercadona.productapi.services.ProductService;
import com.mercadona.productapi.validator.EANValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductApiApplicationTests {

    @Test
    void contextLoads() {
    }
    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductEntity productEntity;
    @Mock
    private ProductMapper productMapper;

    @Mock
    private ConstraintValidatorContext context;


    @BeforeEach
    public void setup() {
        context = Mockito.mock(ConstraintValidatorContext.class);
        MockitoAnnotations.openMocks(this);
        productMapper = new ProductMapperImpl();
    }

    @Test
    public void testGetDestination() {
        Product product = new Product("1234567890123", "Test Product", "Description", 1.9);
        assertEquals("Mercadona España", product.getDestination());

        product = new Product("1234567890126", "Test Product", "Description", 1.9);
        assertEquals("Mercadona Portugal", product.getDestination());

        product = new Product("1234567890128", "Test Product", "Description", 1.9);
        assertEquals("Almacenes", product.getDestination());

        product = new Product("1234567890129", "Test Product", "Description", 1.9);
        assertEquals("Oficinas Mercadona", product.getDestination());

        product = new Product("1234567890120", "Test Product", "Description", 1.9);
        assertEquals("Colmenas", product.getDestination());
    }

    @Test
    public void testSupplier() {
        Product product = new Product("1234567890123", "Test Product", "Description", 1.9);
        assertEquals("1234567", product.getSupplier());
    }

    @Test
    public void testInvalidDestination() {
        Product product = new Product("123456789012X", "Test Product", "Description", 1.9);
        assertEquals("Destino desconocido", product.getDestination());
    }


    @Test
    public void testInvalidEan() {
        try {
            Product product = new Product("1234567", "Test Product", "Description", 1.9);
        } catch (ConstraintViolationException e) {
            assertEquals("El código de barras EAN debe tener 13 dígitos, Código de barras EAN inválido", e.getMessage());
        }
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product product = new Product("1234567890123", "Test Product Create", "Description", 1.9);
        Mockito.doNothing().when(productService).create(product);
        JSONResult<Boolean> result = productController.createProduct(product);
        assertNotNull(result);
        assertEquals(result.getMessage(), "Product created successfully");
    }

    @Test
    public void testGetProductByEan(){
        String ean = "8436019588902";
        JSONResult<Product> response = productController.getProductByEan(ean);
        // Assert
        assertNotNull(response);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product("1234567890123", "Test Product Update", "Description", 1.9);
        Mockito.doNothing().when(productService).updateProduct(product);
        JSONResult<Product> result = productController.updateProduct(product);
        assertEquals(result.getMessage(), "Product update successfully");
    }

    @Test
    public void testProductEntityConstructor() {
        String ean = "1234567890123";
        String name = "Product Name";
        String description = "Product Description";
        Double price = 10.50;

        ProductEntity product = new ProductEntity(ean, name, description, price);

        Assertions.assertEquals(ean, product.getEan());
        Assertions.assertEquals(name, product.getName());
        Assertions.assertEquals(description, product.getDescription());
        Assertions.assertEquals(price, product.getPrice());
    }

    @Test
    public void testEANValidatorValidEAN() {
        EANValidator validator = new EANValidator();
        boolean result = validator.isValid("8410013000564", context);
        Assertions.assertTrue(result);
        Mockito.verifyNoInteractions(context);
    }

    @Test
    public void testEANValidatorNullEAN() {
        ConstraintValidatorContext.ConstraintViolationBuilder builder = Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
        EANValidator validator = new EANValidator();
        boolean result = validator.isValid(null, context);
        Assertions.assertFalse(result);
        Mockito.verify(context).buildConstraintViolationWithTemplate(UrlMvcConstants.EAN_NULL_MESSAGE);
        Mockito.verify(builder).addConstraintViolation();
    }

    @Test
    public void testEANValidatorInvalidLengthEAN() {
        ConstraintValidatorContext.ConstraintViolationBuilder builder = Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
        EANValidator validator = new EANValidator();
        boolean result = validator.isValid("84100130005", context);
        Assertions.assertFalse(result);
        Mockito.verify(context).buildConstraintViolationWithTemplate(UrlMvcConstants.EAN_LENGTH_MESSAGE);
        Mockito.verify(builder).addConstraintViolation();
    }

    @Test
    public void testEANValidatorNonDigitEAN() {
        ConstraintValidatorContext.ConstraintViolationBuilder builder = Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
        EANValidator validator = new EANValidator();
        boolean result = validator.isValid("84100130005a4", context);
        Assertions.assertFalse(result);
        Mockito.verify(context).buildConstraintViolationWithTemplate(UrlMvcConstants.EAN_DIGITS_ONLY_MESSAGE);
        Mockito.verify(builder).addConstraintViolation();
    }

    @Test
    public void testEANValidatorInvalidLastDigitEAN() {
        ConstraintValidatorContext.ConstraintViolationBuilder builder = Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
        EANValidator validator = new EANValidator();
        boolean result = validator.isValid("8410013000567", context);
        Assertions.assertFalse(result);
    }
    @Test
    public void entityToProduct() {
        // Arrange
        when(productEntity.getEan()).thenReturn("8410013000563");
        when(productEntity.getName()).thenReturn("Test Product");
        when(productEntity.getDescription()).thenReturn("This is a test product");
        when(productEntity.getPrice()).thenReturn(9.99);
        // Act
        Product product = productMapper.entityToProduct(productEntity);
        // Assert
        assertEquals("8410013000563", product.getEan());
        assertEquals("Test Product", product.getName());
        assertEquals("This is a test product", product.getDescription());
        assertEquals(9.99, product.getPrice(), 0.001);
    }

    @Test
    public void entityToProductList() {
        // Arrange
        List<ProductEntity> productEntities = Arrays.asList(
                new ProductEntity("8410013000563", "Test Product 1", "This is test product 1", 9.99),
                new ProductEntity("8410013000564", "Test Product 2", "This is test product 2", 19.99)
        );

        // Act
        List<Product> products = productMapper.entityToProductList(productEntities);

        // Assert
        assertEquals(2, products.size());
        assertEquals("8410013000563", products.get(0).getEan());
        assertEquals("Test Product 1", products.get(0).getName());
        assertEquals("This is test product 1", products.get(0).getDescription());
        assertEquals(9.99, products.get(0).getPrice(), 0.001);
        assertEquals("8410013000564", products.get(1).getEan());
        assertEquals("Test Product 2", products.get(1).getName());
        assertEquals("This is test product 2", products.get(1).getDescription());
        assertEquals(19.99, products.get(1).getPrice(), 0.001);
    }

    @Test
    public void productToEntity(){
        // Arrange
        Product product = new Product("8410013000563", "Test Product", "This is a test product", 9.99);

        // Act
        ProductEntity productEntity = productMapper.productToEntity(product);

        // Assert
        assertEquals("8410013000563", productEntity.getEan());
        assertEquals("Test Product", productEntity.getName());
        assertEquals("This is a test product", productEntity.getDescription());
        assertEquals(9.99, productEntity.getPrice(), 0.001);
    }
    @Test
    public void testDefaultConstructor() {
        JSONResult<Boolean> result = new JSONResult<>();
        Assertions.assertFalse(result.isSuccess());
        Assertions.assertNull(result.getErrorCode());
        Assertions.assertNull(result.getMessage());
    }

    @Test
    public void testBooleanConstructor() {
        JSONResult<Boolean> result = new JSONResult<>(true);
        Assertions.assertTrue(result.isSuccess());
        Assertions.assertNull(result.getErrorCode());
        Assertions.assertNull(result.getMessage());
        Assertions.assertTrue(result.getResponse());
    }

    @Test
    public void testBooleanMessageConstructor() {
        String message = "Test message";
        JSONResult<Boolean> result = new JSONResult<>(true, message);
        Assertions.assertTrue(result.isSuccess());
        Assertions.assertNull(result.getErrorCode());
        Assertions.assertEquals(message, result.getMessage());
    }

    @Test
    public void testObjectConstructor() {
        Integer response = 123;
        JSONResult<Integer> result = new JSONResult<>(response);
        Assertions.assertTrue(result.isSuccess());
        Assertions.assertNull(result.getErrorCode());
        Assertions.assertNull(result.getMessage());
        Assertions.assertEquals(response, result.getResponse());
    }

    @Test
    public void testErrorCodeConstructor() {
        Integer errorCode = 500;
        String message = "Internal server error";
        JSONResult<Boolean> result = new JSONResult<>(errorCode, message);
        Assertions.assertFalse(result.isSuccess());
        Assertions.assertEquals(errorCode, result.getErrorCode());
        Assertions.assertEquals(message, result.getMessage());
        Assertions.assertNull(result.getResponse());
    }

    @Test
    public void testFullConstructor() {
        Boolean success = true;
        Integer errorCode = 400;
        String message = "Bad request";
        String response = "Test response";
        JSONResult<String> result = new JSONResult<>(success, errorCode, message, response);
        Assertions.assertEquals(success, result.isSuccess());
        Assertions.assertEquals(errorCode, result.getErrorCode());
        Assertions.assertEquals(message, result.getMessage());
        Assertions.assertEquals(response, result.getResponse());
    }

    @Test
    public void testSettersAndGettersForSuccess() {
        JSONResult<Boolean> result = new JSONResult<>();
        Boolean success = true;
        result.setSuccess(success);
        Assertions.assertEquals(success, result.isSuccess());
    }

    @Test
    public void testSettersAndGettersForErrorCode() {
        JSONResult<Boolean> result = new JSONResult<>();
        Integer errorCode = 400;
        result.setErrorCode(errorCode);
        Assertions.assertEquals(errorCode, result.getErrorCode());
    }

    @Test
    public void testSettersAndGettersForMessage() {
        JSONResult<Boolean> result = new JSONResult<>();
        String message = "Test message";
        result.setMessage(message);
        Assertions.assertEquals(message, result.getMessage());
    }

    @Test
    public void testSettersAndGettersForResponse() {
        JSONResult<String> result = new JSONResult<>();
        String response = "Test response";
        result.setResponse(response);
        Assertions.assertEquals(response, result.getResponse());
    }

}

