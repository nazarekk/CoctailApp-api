package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.StockIngredientInfo;
import com.netcracker.coctail.model.StockIngredientOperations;
import com.netcracker.coctail.model.StockIngredient;

import java.util.List;

public interface StockIngredientDao {

    void addIngredientToStock(long id, StockIngredientOperations stockIngredientOperations);
    void editStockIngredient(long id, long quantity);
    void removeIngredientFromStock(long id);
    List<StockIngredientInfo> findStockIngredientsByName(long ownerId, String name);
    List<StockIngredient> findExistingStockIngredientById(long ownerId, long ingredientId);
}
