package com.netcracker.coctail.dao;
import com.netcracker.coctail.model.CreateKitchenware;
import com.netcracker.coctail.model.Kitchenware;

import java.util.List;


public interface KitchenwareDao {

    void create(CreateKitchenware kitchenware);

    List<Kitchenware> findKitchenwareByName(String name);

    List<Kitchenware> findKitchenwareById(Long id);

    void editKitchenware(Kitchenware kitchenware);

    void removeKitchenware(Kitchenware kitchenware);

}