package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockIngredient {

    private long id;
    private long userId;
    private long ingredientId;
    private long quantity;
}
