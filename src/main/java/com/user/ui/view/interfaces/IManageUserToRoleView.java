package com.user.ui.view.interfaces;

import java.util.Collection;

import com.user.ui.model.Role;
import com.user.ui.model.User;

public interface IManageUserToRoleView {
  
	public void updateUserComboBox(Collection<User>users);
	public void updateRoleComboBox(Collection<Role>roles);
	public void updateUserView(Collection<User>users);
	public void close();
}
