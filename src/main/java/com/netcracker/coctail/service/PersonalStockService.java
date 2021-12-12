package com.netcracker.coctail.service;

import com.netcracker.coctail.model.StockIngredientInfo;
import com.netcracker.coctail.model.StockIngredient;

import java.util.List;

public interface PersonalStockService {

    boolean addIngredientToStock(long userId, long ingredientId, long quantity);
    boolean editIngredient(long userId, long ingredientId, long quantity);
    boolean removeIngredientFromStock(long userId, long ingredientId);
    long getOwnerIdByToken(String token);
    List<StockIngredientInfo> getStockIngredientsByName(long userId, String name);
    List<StockIngredient> getExistingStockIngredientById(long userId, long ingredientId);
    List<StockIngredientInfo> getStockIngredientsFiltered(long userId, String type, String category);
}
