package com.user.ui.view;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.user.ui.model.Role;
import com.user.ui.presenter.AddRolePresenter;
import com.user.ui.view.interfaces.IAddRoleView;
import com.user.ui.view.interfaces.IRoleView;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


@SpringUI
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddRoleView extends Window implements IAddRoleView {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private AddRolePresenter presenter;
	
	private IRoleView parentView;
	
	private VerticalLayout popupContent;
	private TextField textFieldName;
	private TextField textFieldDescription;
	private Button button;
	private Binder<Role> binder;

	@PostConstruct
	public void post() {
		presenter.setView(this);
		binder = new Binder<Role>();
		popupContent = new VerticalLayout();
		popupContent.setWidth("400px");
		textFieldName= new TextField("Name");
		textFieldName.setWidth("100%");
		textFieldDescription= new TextField("Description");
		textFieldDescription.setWidth("100%");
		
		binder.forField(textFieldName)
	      .withValidator(new StringLengthValidator("Too short", 2, 256))
	      .bind(Role::getName,Role::setName);
		
		binder.forField(textFieldDescription)
	      .withValidator(new StringLengthValidator("Too short", 2, 256))
	      .bind(Role::getName,Role::setDescription);
		
		textFieldName.addValueChangeListener(c->validateFields());
		textFieldDescription.addValueChangeListener(c->validateFields());
		
		popupContent.addComponents(textFieldName,textFieldDescription);
		setContent(popupContent);
		center();
		
	}
	

	public void editRole(Role roleForEdit) {
		popupContent.removeAllComponents();
		textFieldName.setValue(roleForEdit.getName());
		textFieldDescription.setValue(roleForEdit.getDescription());
		button= new Button("Edit",click -> presenter.editRole(roleForEdit.getId(),
				                                                  getBindRole()));
		button.setEnabled(false);
		popupContent.addComponents(textFieldName,textFieldDescription,button);
	}
	public void addRole() {
		popupContent.removeAllComponents();
		textFieldName.clear();
		textFieldDescription.clear();
		button= new Button("Add",click -> presenter.addRole(this.getBindRole()));
		button.setEnabled(false);
		popupContent.addComponents(textFieldName,textFieldDescription,button);
	}
	public void setParentView(IRoleView parentView) {
		this.parentView=parentView;
	}

	@Override
	public void updateParantViewData(Collection<Role> roles) {
		parentView.updateData(roles);
		
	}
	private void validateFields() {
		if(button != null) {
			if(binder.isValid()) {
				button.setEnabled(true);
				return;
			}
			button.setEnabled(false);
		}
	}
	private Role getBindRole() {
		Role role = new Role();
		try {
			binder.writeBean(role);
		} catch (ValidationException e) {
			
		}
		return role;
	}
	
}
