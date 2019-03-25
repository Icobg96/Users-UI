package com.user.ui.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.user.ui.client.RoleRestClient;
import com.user.ui.client.UserRestClient;
import com.user.ui.model.Role;
import com.user.ui.model.User;
import com.user.ui.view.interfaces.IManageUserToRoleView;
import com.vaadin.ui.Notification;

@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ManageUserToRolePresenter {
	@Autowired
	private RoleRestClient roleService;
	@Autowired
	private UserRestClient userService;
	
	private IManageUserToRoleView view;
	
	public void setView(IManageUserToRoleView view) {
		this.view=view;
	}
	public void addRoleToUser(User user,Role role) {
		if(user != null && role != null) {
			user.getRoles().add(role);
			userService.updateJsonUserRoles(user);
			view.updateUserView(userService.getAllJsonUsers());
			view.close();
			Notification.show("Successfully added role : "+role.getName()+" on user : "+user.getUserName(),Notification.Type.TRAY_NOTIFICATION);
		}
	}
	public void getUsers() {
	  view.updateUserComboBox(userService.getAllJsonUsers());
	}
	public void getRoles() {
	  view.updateRoleComboBox(roleService.getAllJsonRoles());
	}

}
