package com.user.ui.view;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.user.ui.model.User;
import com.user.ui.presenter.AddUserPresenter;
import com.user.ui.view.interfaces.IAddUserView;
import com.user.ui.view.interfaces.IUserView;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SpringUI
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddUserView extends Window implements IAddUserView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private AddUserPresenter presenter;
	
	private IUserView parentView;
	
	private VerticalLayout popupContent;
	private TextField textFieldUserName;
	private TextField textFielFirstName;
	private TextField textFielLastName;
	private TextField textFielAge;
	private Button button;
	private Binder<User> binder;
	
	@PostConstruct
	public void post() {
		presenter.setView(this);		
		binder = new Binder<User>();	
		popupContent = new VerticalLayout();
		popupContent.setWidth("400px");
		textFieldUserName =new TextField("User Name");
		textFieldUserName.setWidth("100%");
		textFielFirstName = new TextField("First Name");
		textFielFirstName.setWidth("100%");
		textFielLastName = new TextField("Last Name");
		textFielLastName.setWidth("100%");
	    textFielAge =new TextField("Age");
		textFielAge.setWidth("100%");
		
		binder.forField(textFieldUserName)
		      .withValidator(new StringLengthValidator("Too short", 2, 256))
		      .bind(User::getUserName,User::setUserName);
		
			binder.forField(textFielFirstName)
		      .withValidator(new StringLengthValidator("Too short", 2, 256))
		      .bind(User::getFirstName,User::setFirstName);
			
			binder.forField(textFielLastName)
		      .withValidator(new StringLengthValidator("Too short", 2, 256))
		      .bind(User::getLastName,User::setLastName);
		
		binder.forField(textFielAge)
		      .withConverter(new StringToIntegerConverter("Must enter a number"))
		      .withValidator(age->(age > -1),"Age must be a positive number")
		      .bind(User::getAge, User::setAge);
		
		textFieldUserName.addValueChangeListener(c->this.validateFields());
		textFielFirstName.addValueChangeListener(c->this.validateFields());
		textFielLastName.addValueChangeListener(c->this.validateFields());
		textFielAge.addValueChangeListener(c->this.validateFields());		
      
		popupContent.addComponents(textFieldUserName,textFielFirstName,textFielLastName,textFielAge);
		setContent(popupContent);
		center();
	}
	
	public void editUser(User userForEdit) {
		popupContent.removeAllComponents();
		textFieldUserName.setValue(userForEdit.getUserName());
		textFielFirstName.setValue(userForEdit.getFirstName());
		textFielLastName .setValue(userForEdit.getLastName());
		textFielAge .setValue(Integer.toString(userForEdit.getAge()));
		button= new Button("Edit",click -> presenter.editUser(userForEdit.getId(),
				                                                  getBindRole()));
		
		popupContent.addComponents(textFieldUserName,textFielFirstName,textFielLastName,textFielAge,button);
	}
	public void addUser() {
		popupContent.removeAllComponents();
		textFieldUserName.clear();
		textFielFirstName.clear();
		textFielLastName .clear();
		textFielAge .clear();
		button= new Button("Add",click -> presenter.addUser(this.getBindRole()));
		button.setEnabled(false);
		popupContent.addComponents(textFieldUserName,textFielFirstName,textFielLastName,textFielAge,button);
	}
	public void setParenView(IUserView parentView) {
		this.parentView=parentView;
	}
	@Override
	public void updateParantViewData(Collection<User> users) {
		parentView.updateData(users);
		
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
	private User getBindRole() {
		User user = new User();
		try {
			binder.writeBean(user);
		} catch (ValidationException e) {
			
		}
		return user;
	}

}
