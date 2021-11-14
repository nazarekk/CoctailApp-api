package com.netcracker.coctail.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class ModeratorConfirmation {
  private final Long userid;
  private final String email;
  private final String activation;
  private String password;
  private int roleId;
}
