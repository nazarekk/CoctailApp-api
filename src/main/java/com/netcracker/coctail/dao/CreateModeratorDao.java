package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.ActivateModerator;
import com.netcracker.coctail.model.Moderator;

public interface CreateModeratorDao {
  String create(Moderator moderator);
  void activateModerator(ActivateModerator moderator);
}
