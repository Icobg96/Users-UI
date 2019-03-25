package com.user.ui.presenter;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.user.ui.client.RoleRestClient;
import com.user.ui.model.Role;
import com.user.ui.view.AddRoleView;
import com.user.ui.view.interfaces.IRoleView;
import com.vaadin.ui.UI;

@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RolePresenter {
	@Autowired
	private RoleRestClient service;
	@Autowired
	private AddRoleView addRoleView;
	
	private IRoleView view;
	
	public void setView(IRoleView view) {
		this.view=view;
	}
	public void getRoles(){
		view.updateData(service.getAllJsonRoles());
	}
	
	public void addRole() {
		addRoleView.setParentView(view);
		addRoleView.addRole();
		
		if(!addRoleView.isAttached()) {
			UI.getCurrent().addWindow(addRoleView);
		}
	}
	public void editRole(Long id) {
		Role role =service.getJsonRole(id);
		addRoleView.editRole(role);
		addRoleView.setParentView(view);
		
		if(!addRoleView.isAttached()) {
			UI.getCurrent().addWindow(addRoleView);
		}
	}
	public void deleteRole(Long id) {
		service.deleteJsonRole(id);
		view.updateData(service.getAllJsonRoles());
	}
	public void filter(String title,String description) {
		view.updateData(service.getAllJsonRoles()
	    		    .stream()
	    			.filter(role->(((role.getName() != null) ? role.getName().contains(title) : false)
	    					       && ((role.getDescription() != null) ? role.getDescription().contains(description) : false)))
	    			.collect(Collectors.toList()));
		
	}

}
