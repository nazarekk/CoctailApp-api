package com.netcracker.coctail.controllers;


import com.netcracker.coctail.dao.KitchenwareDao;
import com.netcracker.coctail.dto.KitchenwareDto;
import com.netcracker.coctail.model.Kitchenware;
import com.netcracker.coctail.service.KitchenwareService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;

@RestController
public class KitchenwareRestController {
    @Resource
    KitchenwareDao kitchenwareDao;
    KitchenwareService kitchenwareService;
    Kitchenware kitchenware;

    @GetMapping("/kitchenware")

    public Collection<Kitchenware> kitchenwareList() {
        return kitchenwareDao.getKitchenware();
    }
    @GetMapping (value = "/api/kitchenware/{id}")
    public KitchenwareDto getKitchenwareById(@PathVariable(name = "id")long id){
        Kitchenware kitchenware = kitchenwareService.getKitchenwareById(id);
        KitchenwareDto result = KitchenwareDto.fromKitchenware(kitchenware);
        return result;
    }

    @GetMapping (value = "/api/kitchenware/{name}")
    public KitchenwareDto getKitchenwareByName(@PathVariable(name = "name")String name){
        Kitchenware kitchenware = kitchenwareService.getKitchenwareByName(name);
        KitchenwareDto result = KitchenwareDto.fromKitchenware(kitchenware);
        return result;
    }

    @PostMapping(value = "/api/kitchenware/create")
    public void addKitchenware(@RequestBody @Valid Kitchenware kitchenware) {
        kitchenwareService.addKitchenware(kitchenware);
    }

    @PatchMapping(value = "kitchenware/edit/")
    void update(Kitchenware kitchenware){
        kitchenwareService.editKitchenware(kitchenware);
    }

    @DeleteMapping(value = "/api/kitchenware/remove")
    public void removeIngredient(Kitchenware kitchenware){
        kitchenwareService.removeKitchenware(kitchenware);
    }

}