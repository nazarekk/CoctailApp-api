package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Kitchenware {
    private final Long id;
    private String name;
    private String type;
    private String category;
    private boolean isActive;
}
