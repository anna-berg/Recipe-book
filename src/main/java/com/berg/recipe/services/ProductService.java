package com.berg.recipe.services;

import com.berg.recipe.dao.ProductDao;
import com.berg.recipe.dto.ProductDto;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ProductService {

    private static final ProductService INSTANCE = new ProductService();
    private final ProductDao productDao = ProductDao.getInstance();

    private ProductService() {
    }

    public List<ProductDto> findAll() {
        return productDao.findAll().stream()
                .map(product -> ProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .type(product.getType())
                        .fats(product.getFats())
                        .proteins(product.getProteins())
                        .carbohydrates(product.getCarbohydrates())
                        .build()
                )
                .collect(toList());
    }

    public Optional<ProductDto> findById(Long id) {
        return productDao.findById(id)
                .map(product -> ProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .type(product.getType())
                        .fats(product.getFats())
                        .proteins(product.getProteins())
                        .carbohydrates(product.getCarbohydrates())
                        .build());
    }

    public List<ProductDto> findProductsByRecipe(Long id) {
        return productDao.findProductByRecipe(id).stream()
                .map(product -> ProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .type(product.getType())
                        .fats(product.getFats())
                        .proteins(product.getProteins())
                        .carbohydrates(product.getCarbohydrates())
                        .build()
                )
                .collect(toList());
    }

    public static ProductService getInstance() {
        return INSTANCE;
    }
}
