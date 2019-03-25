package com.user.ui.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.user.ui.client.RoleRestClient;
import com.user.ui.model.Role;
import com.user.ui.view.interfaces.IAddRoleView;
import com.vaadin.ui.Notification;
@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddRolePresenter {
	@Autowired
	private RoleRestClient service;
	
	private IAddRoleView view;
	
	public void setView(IAddRoleView view) {
		this.view=view;
	}
	public void addRole(Role role) {
		        if(!"".equals(role.getName()) && !"".equals(role.getDescription())) {
					service.createJsonRole(role);
					view.updateParantViewData(service.getAllJsonRoles());
					view.close();
					Notification.show("Successfully added user",Notification.Type.TRAY_NOTIFICATION);
					return;
		        }
		        Notification.show("Incorect data!!!",Notification.Type.ERROR_MESSAGE);
	}
	
	public void editRole(Long id,Role role) {
		        if(role.getName() != null) {
			        role.setId(id);
					service.updateJsonRole(role);
					view.updateParantViewData(service.getAllJsonRoles());
					view.close();
					Notification.show("Successfully edited user",Notification.Type.TRAY_NOTIFICATION);
					return;
		        }
		        Notification.show("Incorect data!!!",Notification.Type.ERROR_MESSAGE);
	    	
	}

}
