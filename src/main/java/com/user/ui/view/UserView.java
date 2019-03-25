package com.user.ui.view;

import java.util.Collection;

import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.user.ui.model.Role;
import com.user.ui.model.User;
import com.user.ui.presenter.UserPresenter;
import com.user.ui.view.interfaces.IUserView;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.ButtonRenderer;
@SpringUI
public class UserView extends VerticalLayout implements IUserView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UserPresenter presenter;
	
	private Grid<User> userGrid;
	private Grid<Role> roleGrid;
	private HorizontalLayout gridsLayout;
	
	@PostConstruct
	public void post() {
		this.setHeight("100%");
		presenter.setView(this);
		gridsLayout = new HorizontalLayout();
		gridsLayout.setSizeFull();
		Button addButton = new Button("Add", click ->presenter.addUser());
	    Button editButton = new Button("Edit selected", click ->{
		    	try {
			    	presenter.editUser(userGrid.getSelectionModel()
			    		                   .getFirstSelectedItem()
			    		                   .get()
			    		                   .getId());
		    	}catch (NoSuchElementException e) {
		    		Notification.show("Please select item!!!",Notification.Type.WARNING_MESSAGE);
				}
	    	
	    	});
	    
	    HorizontalLayout horizontalLayout = new HorizontalLayout(addButton,editButton);
		this.addComponents(horizontalLayout,gridsLayout);
		this.setExpandRatio(horizontalLayout, 0);
		this.setExpandRatio(gridsLayout, 10);
		makeGrid();
		makeRoleGrid();
		
	}
	private void makeGrid() {
		userGrid = new Grid<User>();
		userGrid.setSizeFull();
		presenter.getUsers();
		Column<User,String> userNameColumn = userGrid.addColumn(User::getUserName).setCaption("User Name");
		Column<User,String> firstNameColumn = userGrid.addColumn(User::getFirstName).setCaption("First Name");
		Column<User,String> lastNameColumn = userGrid.addColumn(User::getLastName).setCaption("Last Name");
		Column<User,Integer> ageNameColumn = userGrid.addColumn(User::getAge).setCaption("Age");
		userGrid.addColumn(user -> "Edit", new ButtonRenderer<Object>(clickEvent ->presenter.editUser(((User)clickEvent.getItem()).getId())));
		userGrid.addColumn(user -> "Delete", new ButtonRenderer<Object>(clickEvent -> {
			 presenter.deleteUser(((User)clickEvent.getItem()).getId());
			 presenter.getUsers();
		    }));

		HeaderRow filterRow = userGrid.appendHeaderRow();
		TextField userNameField = new TextField();
		TextField firstNameField = new TextField();
		TextField lastNameField = new TextField();
		TextField ageField = new TextField();
		
		userNameField.addValueChangeListener(event -> presenter.filter(userNameField.getValue(), 
				                                                       firstNameField.getValue(), 
				                                                       lastNameField.getValue(), 
				                                                       ageField.getValue()));

		userNameField.setValueChangeMode(ValueChangeMode.EAGER);
		

		filterRow.getCell(userNameColumn).setComponent(userNameField);
		userNameField.setSizeFull();
		userNameField.setPlaceholder("Filter");
		
		
		firstNameField.addValueChangeListener(event ->presenter.filter(userNameField.getValue(), 
														               firstNameField.getValue(), 
														               lastNameField.getValue(), 
														               ageField.getValue()));

		firstNameField.setValueChangeMode(ValueChangeMode.EAGER);

		filterRow.getCell(firstNameColumn).setComponent(firstNameField);
		firstNameField.setSizeFull();
		firstNameField.setPlaceholder("Filter");
		
		
		lastNameField.addValueChangeListener(event -> presenter.filter(userNameField.getValue(), 
														               firstNameField.getValue(), 
														               lastNameField.getValue(), 
														               ageField.getValue()));

		lastNameField.setValueChangeMode(ValueChangeMode.EAGER);

		filterRow.getCell(lastNameColumn).setComponent(lastNameField);
		lastNameField.setSizeFull();
		lastNameField.setPlaceholder("Filter");
		
		
		ageField.addValueChangeListener(event -> presenter.filter(userNameField.getValue(), 
													              firstNameField.getValue(), 
													              lastNameField.getValue(), 
													              ageField.getValue()));

		ageField.setValueChangeMode(ValueChangeMode.EAGER);

		filterRow.getCell(ageNameColumn).setComponent(ageField);
		ageField.setSizeFull();
		ageField.setPlaceholder("Filter");
		
		gridsLayout.addComponent(userGrid);
	}
	private void makeRoleGrid() {
		roleGrid = new Grid<Role>();
		roleGrid.setSizeFull();
		roleGrid.addColumn(Role::getName).setCaption("Role Name");
		roleGrid.addColumn(Role::getDescription).setCaption("Role Description");
		roleGrid.addColumn(role -> "Remove", new ButtonRenderer<Object>(clickEvent -> {
				try {
					presenter.deleteRoleFromUser((Role)clickEvent.getItem(),userGrid.getSelectionModel()
						     .getFirstSelectedItem()
						     .get());
						
					roleGrid.setItems(userGrid.getSelectionModel()
				            .getFirstSelectedItem()
				            .get().getRoles());
					}catch (NoSuchElementException e) {
						Notification.show("Please select user!!!",Notification.Type.WARNING_MESSAGE);
				    }
			 }));
		userGrid.addSelectionListener(c->{
				try {
					roleGrid.setItems(userGrid.getSelectionModel()
		                .getFirstSelectedItem()
		                .get().getRoles());
				}catch (NoSuchElementException e) {
					
				}
			});
		VerticalLayout vl=new VerticalLayout(roleGrid);
		vl.setSizeFull();
		vl.setMargin(new MarginInfo(false, true));
		gridsLayout.addComponent(vl);
		gridsLayout.setExpandRatio(userGrid, 6);
		gridsLayout.setExpandRatio(vl, 4);
	}
	
	@Override
	public void updateData(Collection<User> users) {
		userGrid.setItems(users);
		
	} 
	public void refresh() {
		presenter.getUsers();
	}
	

}
