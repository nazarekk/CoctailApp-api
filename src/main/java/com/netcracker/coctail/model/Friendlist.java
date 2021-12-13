package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friendlist {
    private Long id;
    private Long ownerid;
    private Long friendid;
    private Long statusid;
}