package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockIngredientOperations {

    private long ingredientId;
    private long quantity;
}
