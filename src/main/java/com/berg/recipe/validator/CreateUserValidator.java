package com.berg.recipe.validator;

import com.berg.recipe.dto.CreateUserDto;
import com.berg.recipe.entity.Gender;
import com.berg.recipe.entity.Role;

public class CreateUserValidator implements Validator<CreateUserDto> {

    private static final CreateUserValidator INSTANCE = new CreateUserValidator();

    @Override
    public ValidationResult isValid(CreateUserDto object) {
        var validationResult = new ValidationResult();
        if (object.getGender() == null || Gender.find(object.getGender()).isEmpty()) {
            validationResult.add(Error.of("invalid Gender", "Gender is invalid"));
        }

        if (object.getRole() == null || Role.find(object.getRole()).isEmpty()) {
            validationResult.add(Error.of("invalid role", "Role is invalid"));
        }
        return validationResult;
    }

    public static CreateUserValidator getInstance() {
        return INSTANCE;
    }
}
