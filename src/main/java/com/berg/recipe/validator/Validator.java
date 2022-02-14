package com.berg.recipe.validator;

public interface Validator<T> {

    ValidationResult isValid(T object);
}
