package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Kitchenware {
    private final Long id;
    private final String name;
    private final String type;
    private final String category;
    private final boolean isActive;
    private final String image;
}
