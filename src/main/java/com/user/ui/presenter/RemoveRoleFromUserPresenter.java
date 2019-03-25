package com.user.ui.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.user.ui.client.UserRestClient;
import com.user.ui.model.Role;
import com.user.ui.model.User;
import com.user.ui.view.interfaces.IRemoveRoleFromUserView;
import com.vaadin.ui.Notification;

@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RemoveRoleFromUserPresenter {

	@Autowired
	private UserRestClient userService;
	
	private IRemoveRoleFromUserView view;
	
	public void setView(IRemoveRoleFromUserView view) {
		this.view=view;
		
	}

	public void removeRoleToUser(User user,Role role) {
		if(user != null && role != null) {
			user.getRoles().remove(role);
			userService.updateJsonUserRoles(user);
			view.updateUserView(userService.getAllJsonUsers());
			view.close();
			Notification.show("Successfully remove role : "+role.getName()+" from user : "+user.getUserName(),Notification.Type.TRAY_NOTIFICATION);
		}
	}
	public void getUsers() {
	  view.updateUserComboBox(userService.getAllJsonUsers());
	}
	public void getRoles() {
	  view.updateRoleComboBox();
	}

}
