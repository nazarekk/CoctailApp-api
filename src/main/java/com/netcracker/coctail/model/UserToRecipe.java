package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserToRecipe {
    private Integer id;
    private Integer userId;
    private Integer recipeId;
    private boolean liked;
}