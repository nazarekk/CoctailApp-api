package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friendlist {
    private final Long id;
    private final Long ownerid;
    private final Long friendid;
    private final Long statusid;
}