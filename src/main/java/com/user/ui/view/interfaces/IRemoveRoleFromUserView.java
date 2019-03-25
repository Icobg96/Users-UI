package com.user.ui.view.interfaces;

import java.util.Collection;

import com.user.ui.model.User;

public interface IRemoveRoleFromUserView {
	public void updateUserComboBox(Collection<User>users);
	public void updateRoleComboBox();
	public void updateUserView(Collection<User>users);
	public void close();

}
