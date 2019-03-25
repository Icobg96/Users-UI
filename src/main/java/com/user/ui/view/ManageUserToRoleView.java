package com.user.ui.view;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.user.ui.model.Role;
import com.user.ui.model.User;
import com.user.ui.presenter.ManageUserToRolePresenter;
import com.user.ui.view.interfaces.IManageUserToRoleView;
import com.user.ui.view.interfaces.IUserView;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SpringUI
public class ManageUserToRoleView extends Window implements IManageUserToRoleView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private ManageUserToRolePresenter presenter;
	
	private VerticalLayout popupContent;
	private ComboBox<User> userComboBox;
	private ComboBox<Role> roleComboBox;
	private Button addButton;
	private IUserView userView;
	
	@PostConstruct
	public void post(){
		presenter.setView(this);
		popupContent=new VerticalLayout();
		popupContent.setWidth("400px");
		userComboBox=new ComboBox<User>();
		userComboBox.setCaption("User");
		userComboBox.setWidth("100%");
		userComboBox.setItemCaptionGenerator(User::getUserName);
		roleComboBox = new ComboBox<Role>();
		roleComboBox.setCaption("Role");
		roleComboBox.setWidth("100%");
		roleComboBox.setItemCaptionGenerator(Role::getName);
		addButton= new Button("Add",click->{presenter.addRoleToUser(
				userComboBox.getValue(),roleComboBox.getValue());
		        userComboBox.clear();
		        roleComboBox.clear();
		        
		        });
		popupContent.addComponents(userComboBox,roleComboBox,addButton);
		this.setContent(popupContent);
		center();
		
	}
	

	@Override
	public void updateUserComboBox(Collection<User> users) {
		
		userComboBox.setItems(users);
	}

	@Override
	public void updateRoleComboBox(Collection<Role> roles) {
		
		roleComboBox.setItems(roles);
	}
	public void fillCombo() {
		presenter.getUsers();
		presenter.getRoles();
	}
	public void setUserView(IUserView userView) {
		this.userView=userView;
	}


	@Override
	public void updateUserView(Collection<User> users) {
		userView.updateData(users);
		
	}
    
}
