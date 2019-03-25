package com.user.ui.view;

import java.util.Collection;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.user.ui.model.Role;
import com.user.ui.presenter.RolePresenter;
import com.user.ui.view.interfaces.IRoleView;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.ButtonRenderer;
@SpringUI
public class RoleView extends VerticalLayout implements IRoleView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private RolePresenter presenter;
	
	private Grid<Role> grid;
	
	@PostConstruct
	public void post() {
		this.setHeight("100%");
        presenter.setView(this);
		Button addButton = new Button("Add", click ->presenter.addRole());
	    Button editButton = new Button("Edit selected", click ->{
		    	try {
		    		presenter.editRole(grid.getSelectionModel()
			                               .getFirstSelectedItem()
										   .get()
										   .getId());
				} catch (NoSuchElementException e) {
					Notification.show("Please select item!!!",Notification.Type.WARNING_MESSAGE);
				}
	    	
	       });
	    HorizontalLayout horizontalLayout = new HorizontalLayout(addButton,editButton);
		this.addComponent(horizontalLayout);
		makeGrid();
		this.setExpandRatio(horizontalLayout, 0);
		this.setExpandRatio(grid, 10);
		
		
	}
	
	private void makeGrid() {
		grid = new Grid<Role>();
		grid.setWidth("50%");	
		grid.setHeight("100%");
		presenter.getRoles();
		Column<Role,String> titleColumn = grid.addColumn(Role::getName).setCaption("Name");
		Column<Role,String> descriptionColumn = grid.addColumn(Role::getDescription).setCaption("Description");

		grid.addColumn(user -> "Edit", new ButtonRenderer<Object>(clickEvent ->presenter.editRole(((Role)clickEvent.getItem()).getId())));
		grid.addColumn(user -> "Delete", new ButtonRenderer<Object>(clickEvent -> {
			presenter.deleteRole(((Role)clickEvent.getItem()).getId());
			presenter.getRoles();
		}));

		HeaderRow filterRow = grid.appendHeaderRow();
		
		TextField titleField = new TextField();
		TextField descriptionField = new TextField();
		
		titleField.addValueChangeListener(event -> presenter.filter(titleField.getValue(),
				                                                    descriptionField.getValue()));

		titleField.setValueChangeMode(ValueChangeMode.EAGER);
		

		filterRow.getCell(titleColumn).setComponent(titleField);
		//titleField.setSizeFull();
		titleField.setPlaceholder("Filter");
		
	
		
		descriptionField.addValueChangeListener(event -> presenter.filter(titleField.getValue(),
				                                                    descriptionField.getValue()));

		descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
		

		filterRow.getCell(descriptionColumn).setComponent(descriptionField);
		//titleField.setSizeFull();
		descriptionField.setPlaceholder("Filter");

		this.addComponent(grid);
	}

	@Override
	public void updateData(Collection<Role> roles) {
		grid.setItems(roles);
		
	}
}
