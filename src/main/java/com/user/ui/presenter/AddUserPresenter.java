package com.user.ui.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.user.ui.client.UserRestClient;
import com.user.ui.model.User;
import com.user.ui.view.interfaces.IAddUserView;
import com.vaadin.ui.Notification;

@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddUserPresenter {
	@Autowired
	private UserRestClient service;
	
	private IAddUserView view;
	
	public void setView(IAddUserView view) {
		this.view=view;
	}
	public void addUser(User user) {
		        if(user != null) {
					service.createJsonUser(user);
					view.updateParantViewData(service.getAllJsonUsers());
					view.close();
					Notification.show("Successfully added user",Notification.Type.TRAY_NOTIFICATION);
					return;
		        }
		        Notification.show("Incorect data!!!",Notification.Type.ERROR_MESSAGE);
	}
	
	public void editUser(Long id,User user) {
		        if(user != null) {
			        user.setId(id);
					service.updateJsonUser(user);
					view.updateParantViewData(service.getAllJsonUsers());
					view.close();
					Notification.show("Successfully edited user",Notification.Type.TRAY_NOTIFICATION);
					return;
		        }
	    	
	}

}
