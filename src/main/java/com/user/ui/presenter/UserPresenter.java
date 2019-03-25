package com.user.ui.presenter;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.user.ui.client.UserRestClient;
import com.user.ui.model.Role;
import com.user.ui.model.User;
import com.user.ui.view.AddUserView;
import com.user.ui.view.UserView;
import com.user.ui.view.interfaces.IUserView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserPresenter  {
	@Autowired
	private UserRestClient service;
	
	@Autowired
	private AddUserView addUserView;
	
	private IUserView view;
	
	public void setView(UserView userView) {
		this.view=userView;
		
	}
	public void getUsers(){
		view.updateData(service.getAllJsonUsers());
	}
	
	public void addUser() {
		addUserView.addUser();
		addUserView.setParenView(view);
		
		if(!addUserView.isAttached()) {
			UI.getCurrent().addWindow(addUserView);
		}
		
	}
	public void editUser(Long id) {
		User user =service.getJsonUser(id);
		addUserView.editUser(user);
		addUserView.setParenView(view);
		
		if(!addUserView.isAttached()) {
			UI.getCurrent().addWindow(addUserView);
		}
	}
	public void deleteUser(Long id) {
		service.deleteJsonUser(id);
		view.updateData(service.getAllJsonUsers());
	}
	public void filter(String userName,String firstName,String lastName,String age) {
		try {
		view.updateData(service.getAllJsonUsers()
	    		    .stream()
	    			.filter(user->(((user.getUserName() != null) ? user.getUserName().contains(userName) : false)
    					    && ((user.getFirstName() != null) ? user.getFirstName().contains(firstName) : false)
    					    && ((user.getLastName() != null) ? user.getLastName().contains(lastName) : false )
	    					&& user.getAge()==Integer.parseUnsignedInt(age)))
	    			.collect(Collectors.toList()));
		
		}catch (NumberFormatException e) {
			view.updateData(service.getAllJsonUsers()
	    		    .stream()
	    			.filter(user->(((user.getUserName() != null) ? user.getUserName().contains(userName) : false)
	    					    && ((user.getFirstName() != null) ? user.getFirstName().contains(firstName) : false)
	    					    && ((user.getLastName() != null) ? user.getLastName().contains(lastName) : false )))
	    			.collect(Collectors.toList()));
		}
	}

	
	
	public void deleteRoleFromUser(Role role, User user) {
		if(user != null && role != null) {
			user.getRoles().remove(role);
			service.updateJsonUserRoles(user);	
			Notification.show("Successfully remove role : "+role.getName()+" from user : "+user.getUserName(),Notification.Type.TRAY_NOTIFICATION);
		}
		
	}


}
