package com.netcracker.coctail.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class ModeratorInformation {
  private final Long userid;
  private final String email;
  private final String Nickname;
  private final Boolean IsActive;
}
