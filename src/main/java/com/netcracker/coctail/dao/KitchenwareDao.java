package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;


public interface KitchenwareDao {
    int create(Kitchenware kitchenware);

    Collection<Kitchenware> getKitchenware();

    List<Kitchenware> findKitchenwareByName(String name);

    List<Kitchenware> findKitchenwareById(Long id);

    void editKitchenware(Kitchenware kitchenware);

    void removeKitchenware(Kitchenware kitchenware);

    Kitchenware filterKitchenware(Boolean isactive);
}