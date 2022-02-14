package com.berg.recipe.validator;

import com.berg.recipe.dto.CreateUserDto;
import com.berg.recipe.entity.Gender;

public class CreateUserValidator implements Validator<CreateUserDto> {

    private static final CreateUserValidator INSTANCE = new CreateUserValidator();

    @Override
    public ValidationResult isValid(CreateUserDto object) {
        var validationResult = new ValidationResult();
        if (Gender.find(object.getGender()).isPresent()) {
            validationResult.add(Error.of("invalid Gender", "Gender is invalid"));
        }
        return validationResult;
    }

    public static CreateUserValidator getInstance() {
        return INSTANCE;
    }
}
