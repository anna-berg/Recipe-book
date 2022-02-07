package com.berg.recipe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DailyMenu {

    private Long id;
    private Recipe breakfast;
    private Recipe firstSnack;
    private Recipe lunch;
    private Recipe secondSnack;
    private Recipe dinner;
}
