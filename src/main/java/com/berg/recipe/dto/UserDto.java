package com.berg.recipe.dto;

import com.berg.recipe.entity.Gender;
import com.berg.recipe.entity.Role;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {

    Long id;
    String name;
    String eMail;
    Role role;
    Gender gender;
}
