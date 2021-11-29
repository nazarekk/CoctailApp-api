package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.KitchenwareDao;
import com.netcracker.coctail.exceptions.InvalidEmailOrPasswordException;
import com.netcracker.coctail.model.Kitchenware;
import com.netcracker.coctail.service.KitchenwareService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.beans.PropertyEditorSupport;
import java.util.List;

@Service
@Slf4j
@Data
public class KitchenwareServiceImp implements KitchenwareService {

    private final KitchenwareDao kitchenwareDao;

    @Override
    public Kitchenware getKitchenwareById(Long id){
        Kitchenware result = kitchenwareDao.findKitchenwareById(id).get(0);
        if (result == null) {
            log.warn("IN getKitchenwareById - no ingredient found by id: {}", id);
            return null;
        }
        return result;
    }

    @Override
    public Kitchenware getKitchenwareByName(String name){
        List<Kitchenware> result = kitchenwareDao.findKitchenwareByName(name);
        if (result.isEmpty()) {
            throw new InvalidEmailOrPasswordException();
        }
        log.info("IN getKitchenwareByName - ingredient: {} found by name: {}", result.get(0).getName(), result.get(0).getId());
        return result.get(0);
    }

    @Override
    public void addKitchenware(Kitchenware kitchenware){
        String result = kitchenware.getName();
        if (kitchenwareDao.findKitchenwareByName(result).isEmpty()){
            kitchenwareDao.create(kitchenware);
        }
        else {
            throw new InvalidEmailOrPasswordException();
            //type,category
        }
    }

    public void editKitchenware(Kitchenware kitchenware){
        kitchenwareDao.editKitchenware(kitchenware);
    }
    public void removeKitchenware(Kitchenware kitchenware){
        kitchenwareDao.removeKitchenware(kitchenware);
    }

}