package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.ActivateModerator;
import com.netcracker.coctail.model.Moderator;
import com.netcracker.coctail.model.ModeratorInformation;
import java.util.Collection;

public interface ModeratorDao {
  int create(Moderator moderator);

  void activateModerator(ActivateModerator moderator);

  Collection<ModeratorInformation> moderatorList();

  void editModerator(ModeratorInformation moderator);

  void removeModerator(ModeratorInformation moderator);

  ModeratorInformation searchModerator(String value);
}
