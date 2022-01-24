package com.berg.recipe.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Product {

    private Long id;
    private String name;
    private Integer proteins;
    private Integer fats;
    private Integer carbohydrates;
    private String type;
}
