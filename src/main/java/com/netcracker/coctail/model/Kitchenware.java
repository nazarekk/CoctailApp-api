package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Kitchenware {
    private final Long id;;
    private String nickname;
    private String type;
    private String category;
    private boolean isactive;
}
