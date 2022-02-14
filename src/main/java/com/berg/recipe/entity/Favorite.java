package com.berg.recipe.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Favorite {

    private Long id;
    private User user;
    private DailyMenu dailyMenus;
    private Recipe recipes;
    private int rating;
    private LocalDateTime createdAt;
}
