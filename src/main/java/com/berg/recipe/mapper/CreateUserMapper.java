package com.berg.recipe.mapper;

import com.berg.recipe.dto.CreateUserDto;
import com.berg.recipe.entity.Gender;
import com.berg.recipe.entity.Role;
import com.berg.recipe.entity.User;

public class CreateUserMapper implements Mapper<CreateUserDto, User>{

    private static final CreateUserMapper INSTANCE = new CreateUserMapper();

    @Override
    public User mapFrom(CreateUserDto object) {
        return User.builder()
                .name(object.getName())
                .eMail(object.getEMail())
                .password(object.getPassword())
                .gender(Gender.valueOf(object.getGender()))
                .role(Role.valueOf(object.getRole()))
                .build();
    }

    public static CreateUserMapper getInstance() {
        return INSTANCE;
    }
}
