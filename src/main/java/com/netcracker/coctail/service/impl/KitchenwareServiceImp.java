package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.KitchenwareDao;
import com.netcracker.coctail.exceptions.InvalidEmailOrPasswordException;
import com.netcracker.coctail.model.CreateKitchenware;
import com.netcracker.coctail.model.Kitchenware;
import com.netcracker.coctail.service.KitchenwareService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@Data
public class KitchenwareServiceImp implements KitchenwareService {

    private final KitchenwareDao kitchenwareDao;

    @Override
    public Kitchenware getKitchenwareById(Long id) {
        Kitchenware result = kitchenwareDao.findKitchenwareById(id).get(0);
        if (result == null) {
            log.warn("IN getKitchenwareById - no kitchenware found by id: {}", id);
            return null;
        }
        return result;
    }

    @Override
    public List<Kitchenware> getKitchenwareByName(String name) {
        List<Kitchenware> result = kitchenwareDao.findKitchenwareByName(name);
        if (result.isEmpty()) {
            throw new InvalidEmailOrPasswordException();
        }
        return result;
    }

    @Override
    public void addKitchenware(CreateKitchenware kitchenware) {
        String name = kitchenware.getName();
        log.info("Kitchenware with name " + name + " already exists");
        if (kitchenwareDao.findKitchenwareByName(name).isEmpty()) {
            kitchenwareDao.create(kitchenware);
        } else {
            log.info("Kitchenware with name " + name + " already exists");
            throw new InvalidEmailOrPasswordException();
        }
    }

    @Override
    public void editKitchenware(Kitchenware kitchenware) {
        log.info("Kitchenware isActive = " + kitchenware.isActive());
        long id = kitchenware.getId();
        Kitchenware result = kitchenwareDao.findKitchenwareById(id).get(0);
        if (result == null) {
            log.info("Kitchenware with id " + id + " doesn't exists");
            throw new InvalidEmailOrPasswordException();
        }
        if (kitchenware.getName().equals("")) {
            kitchenware.setName(result.getName());
        }
        if (kitchenware.getType().equals("")) {
            kitchenware.setType(result.getType());
        }
        if (kitchenware.getCategory().equals("")) {
            kitchenware.setCategory(result.getCategory());
        }
        log.info("Kitchenware isActive = " + kitchenware.isActive());
        kitchenwareDao.editKitchenware(kitchenware);
    }

    @Override
    public void removeKitchenware(long id) {
        Kitchenware result = kitchenwareDao.findKitchenwareById(id).get(0);
        if (result == null) {
            log.info("Kitchenware with id " + id + " doesn't exists");
            throw new InvalidEmailOrPasswordException();
        }
        kitchenwareDao.removeKitchenware(result);
    }


}