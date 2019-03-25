package com.user.ui.view.interfaces;

import java.util.Collection;

import com.user.ui.model.Role;

public interface IAddRoleView {
	public void updateParantViewData(Collection<Role> roles);
	public void close();
}
