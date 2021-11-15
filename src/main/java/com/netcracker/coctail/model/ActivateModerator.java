package com.netcracker.coctail.model;

import lombok.Data;

@Data
public class ActivateModerator {
  private final String password;
  private final String code;
}
