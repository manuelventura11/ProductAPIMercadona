package com.mercadona.productapi.common.constants;

public class UrlMvcConstants {
    public static final String PRODUCTS = "/products";

    public static final String EAN = "/{ean}";
    public static final String CREATE = "/create";
    public static final String DELETE = "/delete" +EAN ;
    public static final String UPDATE = "/update" ;
    public static final String EAN_NULL_MESSAGE = "El código de barras EAN no puede ser nulo";
    public static final String EAN_LENGTH_MESSAGE = "El código de barras EAN debe tener 13 dígitos";
    public static final String EAN_DIGITS_ONLY_MESSAGE = "El código de barras EAN debe contener solo dígitos";
}
