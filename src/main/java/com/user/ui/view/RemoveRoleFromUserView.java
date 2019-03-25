package com.user.ui.view;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.user.ui.model.Role;
import com.user.ui.model.User;
import com.user.ui.presenter.RemoveRoleFromUserPresenter;
import com.user.ui.view.interfaces.IRemoveRoleFromUserView;
import com.user.ui.view.interfaces.IUserView;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
@SpringUI
public class RemoveRoleFromUserView extends Window implements IRemoveRoleFromUserView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private RemoveRoleFromUserPresenter presenter;
	
	private VerticalLayout popupContent;
	private ComboBox<User> userComboBox;
	private ComboBox<Role> roleComboBox;
	private Button removeButton;
	private IUserView userView;
	
	@PostConstruct
	public void post() {
		presenter.setView(this);
		popupContent=new VerticalLayout();
		popupContent.setWidth("400px");
		userComboBox=new ComboBox<User>();
		userComboBox.setCaption("User");
		userComboBox.setWidth("100%");
		userComboBox.setItemCaptionGenerator(User::getUserName);
	    userComboBox.addValueChangeListener(change->{
	    	roleComboBox.clear();
	    	presenter.getRoles();
	    	});
		roleComboBox = new ComboBox<Role>();
		roleComboBox.setCaption("Role");
		roleComboBox.setWidth("100%");
		roleComboBox.setItemCaptionGenerator(Role::getName);
		removeButton= new Button("Remove",click->{
			    presenter.removeRoleToUser(userComboBox.getValue(),roleComboBox.getValue());
		        userComboBox.clear();
		        roleComboBox.clear();
		        });
		popupContent.addComponents(userComboBox,roleComboBox,removeButton);
		this.setContent(popupContent);
		center();
		
	}
	public void setUserView(IUserView userView) {
		this.userView=userView;
	}

	@Override
	public void updateUserComboBox(Collection<User> users) {
		userComboBox.setItems(users);
		
	}

	@Override
	public void updateRoleComboBox() {
		if(userComboBox.getValue()!=null) {
			roleComboBox.setItems(userComboBox.getValue().getRoles());
		}
		
	}
	public void fillCombo() {
		presenter.getUsers();
	}

	@Override
	public void updateUserView(Collection<User> users) {
		userView.updateData(users);
		
	}

}
