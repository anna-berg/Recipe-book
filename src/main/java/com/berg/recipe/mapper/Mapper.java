package com.berg.recipe.mapper;

public interface Mapper<F, T> {

    T mapFrom(F object);
}
