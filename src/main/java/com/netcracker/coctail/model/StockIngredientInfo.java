package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockIngredientInfo {

    private long id;
    private String name;
    private String type;
    private String category;
    private boolean isActive;
    private long quantity;
    private String image;
}
