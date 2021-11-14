package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Moderator;
import com.netcracker.coctail.model.ModeratorConfirmation;
import com.netcracker.coctail.model.ReadUser;
import java.util.Collection;
import java.util.List;

public interface CreateModeratorDao {
  String create(Moderator moderator);
  void activateModerator(String code, ModeratorConfirmation user);
  public List<ReadUser> getByCode(String code);
}
