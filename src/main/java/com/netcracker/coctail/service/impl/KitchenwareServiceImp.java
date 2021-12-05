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
        List<Kitchenware> result = kitchenwareDao.findAllKitchenwareByName(name);
        return result;
    }

    @Override
    public List<Kitchenware> getKitchenwareFiltered(String type, String category) {
        log.info("Filtering");
        List<Kitchenware> result = kitchenwareDao.findAllKitchenwareFiltered(type, category);
        return result;
    }

    @Override
    public Boolean addKitchenware(CreateKitchenware kitchenware) {
        String name = kitchenware.getName();
        if (kitchenwareDao.findKitchenwareByName(name).isEmpty()) {
            kitchenwareDao.create(kitchenware);
            return Boolean.TRUE;
        } else {
            log.info("Kitchenware with name " + name + " already exists");
            throw new InvalidEmailOrPasswordException();
        }
    }

    @Override
    public Boolean editKitchenware(Kitchenware kitchenware) {
        log.info("Kitchenware isActive = " + kitchenware.isActive());
        long id = kitchenware.getId();
        String name = kitchenware.getName();
        Kitchenware result = kitchenwareDao.findKitchenwareById(id).get(0);
        if (result == null) {
            log.info("Kitchenware with id " + id + " doesn't exists");
            throw new InvalidEmailOrPasswordException();
        }
        if (kitchenwareDao.findKitchenwareByName(name).isEmpty()) {
            kitchenwareDao.editKitchenware(kitchenware);
            return Boolean.TRUE;
        } else {
            log.info("Kitchenware with name " + name + " already exists");
            throw new InvalidEmailOrPasswordException();
        }
    }

    @Override
    public Boolean removeKitchenware(long id) {
        Kitchenware result = kitchenwareDao.findKitchenwareById(id).get(0);
        if (result == null) {
            log.info("Kitchenware with id " + id + " doesn't exists");
            throw new InvalidEmailOrPasswordException();
        }
        kitchenwareDao.removeKitchenware(result);
        return Boolean.TRUE;
    }


}