package com.user.ui.view.interfaces;

import java.util.Collection;

import com.user.ui.model.User;

public interface IAddUserView {
	public void updateParantViewData(Collection<User> users);
	public void close();

}
