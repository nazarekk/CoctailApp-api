package com.netcracker.coctail.service;
import com.netcracker.coctail.model.Kitchenware;

public interface KitchenwareService{
    Kitchenware getKitchenwareById(Long id);
    Kitchenware getKitchenwareByName(String name);
    void addKitchenware(Kitchenware kitchenware);
    void editKitchenware(Kitchenware kitchenware);
    void removeKitchenware(Kitchenware kitchenware);
}