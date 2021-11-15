package com.netcracker.coctail.model;

import lombok.Data;

@Data
public class Moderator {
  private final String email;
  private final Boolean isActive;
  private final String activation;
}
