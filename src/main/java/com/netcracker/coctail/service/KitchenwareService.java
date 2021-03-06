package com.netcracker.coctail.service;

import com.netcracker.coctail.model.CreateKitchenware;
import com.netcracker.coctail.model.Kitchenware;

import java.util.List;

public interface KitchenwareService {
    Kitchenware getKitchenwareById(Long id);

    List<Kitchenware> getKitchenwareByName(String name);

    List<Kitchenware> getKitchenwareFiltered(String type, String category, String active);

    Boolean addKitchenware(CreateKitchenware kitchenware);

    Boolean editKitchenware(Kitchenware kitchenware);

    Boolean removeKitchenware(long id);
}