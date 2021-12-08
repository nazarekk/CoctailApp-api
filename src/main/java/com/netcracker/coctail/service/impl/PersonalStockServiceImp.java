package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.FriendlistDao;
import com.netcracker.coctail.dao.StockIngredientDao;
import com.netcracker.coctail.exceptions.UserAlreadyHasStockIngredient;
import com.netcracker.coctail.exceptions.UserDoesNotHaveSuchIngredient;
import com.netcracker.coctail.model.StockIngredientInfo;
import com.netcracker.coctail.model.StockIngredientOperations;
import com.netcracker.coctail.model.StockIngredient;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.PersonalStockService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Data
public class PersonalStockServiceImp implements PersonalStockService {

    private final StockIngredientDao stockIngredientDao;
    private final FriendlistDao friendlistDao;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean addIngredientToStock(long userId, StockIngredientOperations stockIngredientOperations) {
        long ingredientId = stockIngredientOperations.getIngredientId();

        if (stockIngredientDao.findExistingStockIngredientById(userId, ingredientId).isEmpty()) {
            stockIngredientDao.addIngredientToStock(userId, stockIngredientOperations);
            return true;
        } else {
            log.info("User with id " + userId + " has already ingredient with id " + ingredientId + " in stock");
            return false;
        }
    }

    @Override
    public boolean editIngredient(long userId, StockIngredientOperations stockIngredientOperations) {
        long ingredientId = stockIngredientOperations.getIngredientId();
        List<StockIngredient> stockIngredients = getExistingStockIngredientById(userId, ingredientId);
        if (stockIngredients.isEmpty()) {
            log.info("User with id " + userId + " doesn't have ingredient with id " + ingredientId);
            return false;
        } else {
            stockIngredientDao.editStockIngredient(stockIngredients.get(0).getId(),
                    stockIngredientOperations.getQuantity());
            return true;
        }
    }

    @Override
    public boolean removeIngredientFromStock(long userId, long ingredientId) {
        List<StockIngredient> stockIngredients = getExistingStockIngredientById(userId, ingredientId);
        if (stockIngredients.isEmpty()) {
            log.info("User with id " + userId + " doesn't have ingredient with id " + ingredientId);
            return false;
        } else {
            stockIngredientDao.removeIngredientFromStock(stockIngredients.get(0).getId());
            return true;
        }
    }

    @Override
    public long getOwnerIdByToken(String token) {
        String email = jwtTokenProvider.getEmail(token.substring(7));
        return friendlistDao.getOwnerId(email);
    }

    @Override
    public List<StockIngredientInfo> getStockIngredientsByName(long userId, String name) {
        return stockIngredientDao.findStockIngredientsByName(userId, name);
    }

    @Override
    public List<StockIngredient> getExistingStockIngredientById(long userId, long ingredientId) {
        return stockIngredientDao.findExistingStockIngredientById(userId, ingredientId);
    }

    @Override
    public List<StockIngredientInfo> getStockIngredientsFiltered(long userId, String type, String category) {
        return stockIngredientDao.findStockIngredientsFiltered(userId, type, category);
    }

}
