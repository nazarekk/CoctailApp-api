package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.KitchenwareDao;
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
    return kitchenwareDao.findAllKitchenwareByName(name);
  }

  @Override
  public List<Kitchenware> getKitchenwareFiltered(String type, String category, String active) {
    return kitchenwareDao.findAllKitchenwareFiltered(type, category, active);
  }

  @Override
  public Boolean addKitchenware(CreateKitchenware kitchenware) {
    String name = kitchenware.getName();
    if (kitchenwareDao.findKitchenwareByName(name).isEmpty()) {
      kitchenwareDao.create(kitchenware);
      log.info("Kitchenware with name {} created", name);
      return Boolean.TRUE;
    } else {
      log.error("Kitchenware with name {} already exists", name);
      return Boolean.FALSE;
    }
  }

  @Override
  public Boolean editKitchenware(Kitchenware kitchenware) {
    long id = kitchenware.getId();
    String name = kitchenware.getName();
    Kitchenware result = kitchenwareDao.findKitchenwareById(id).get(0);
    if (result == null) {
      log.error("Kitchenware with id {} doesn't exists", id);
      return Boolean.FALSE;
    }
    if (kitchenwareDao.findKitchenwareByName(name).isEmpty() | name.equals(result.getName())) {
      kitchenwareDao.editKitchenware(kitchenware);
      return Boolean.TRUE;
    } else {
      log.error("Kitchenware with name {} already exists", name);
      return Boolean.FALSE;
    }

  }

  @Override
  public Boolean removeKitchenware(long id) {
    Kitchenware result = kitchenwareDao.findKitchenwareById(id).get(0);
    if (result == null) {
      log.info("Kitchenware with id {} doesn't exists", id);
      return Boolean.FALSE;
    }
    kitchenwareDao.removeKitchenware(result);
    return Boolean.TRUE;
  }


}