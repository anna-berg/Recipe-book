package com.berg.recipe.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserDto {

    String name;
    String eMail;
    String password;
    String role;
    String gender;
}
