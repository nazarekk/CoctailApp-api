package com.netcracker.coctail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import com.netcracker.coctail.model.Kitchenware;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KitchenwareDto {
    private String name;
    private String type;
    private String category;


    public static KitchenwareDto fromKitchenware(Kitchenware kitchenware) {
        KitchenwareDto kitchenwareDto = new KitchenwareDto();
        kitchenwareDto.setName(kitchenware.getName());
        kitchenwareDto.setType(kitchenware.getType());
        kitchenwareDto.setCategory(kitchenware.getCategory());

        return kitchenwareDto;
    }
}
