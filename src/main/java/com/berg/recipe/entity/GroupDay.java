package com.berg.recipe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupDay {

    private Long id;
    private Grouped group;
    private DailyMenu dailyMenu;
    private int position;
}
