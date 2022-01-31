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
public class GroupDay {

    private Long id;
    private Grouped group;
    private DailyMenu dailyMenu;
    private int position;
}
