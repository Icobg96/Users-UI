package com.user.ui.view;


import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;




@SpringUI(path="/users")
@Theme("valo")
public class MainView extends UI{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UserView userView;
	
	@Autowired
	private RoleView roleView;
	@Autowired
	private ManageUserToRoleView manageUserToRoleView;
	@Autowired
	private RemoveRoleFromUserView removeRoleFromUserView;
	
	

	@Override
	protected void init(VaadinRequest request) {
		userView.setVisible(true);
		roleView.setVisible(false);
		MenuBar menu = new MenuBar();
		VerticalLayout leayout = new VerticalLayout();
		leayout.setHeight("100%");
		menu.addItem("Users",c->{
			userView.refresh();
			userView.setVisible(true);
			roleView.setVisible(false);
		});
		menu.addItem("Roles",c->{
			userView.setVisible(false);
			roleView.setVisible(true);
		});
		menu.addItem("Manage User to Role",c->{
			removeRoleFromUserView.close();
			manageUserToRoleView.fillCombo();
			manageUserToRoleView.setUserView(userView);
			
			if(!manageUserToRoleView.isAttached()) {
				UI.getCurrent().addWindow(manageUserToRoleView);
			}
		});
		menu.addItem("Remove User from Role",c->{
			manageUserToRoleView.close();
			removeRoleFromUserView.fillCombo();
			removeRoleFromUserView.setUserView(userView);
			
			if(!removeRoleFromUserView.isAttached()) {
				UI.getCurrent().addWindow(removeRoleFromUserView);
			}
		});

		leayout.addComponents(menu,userView,roleView);
		leayout.setExpandRatio(menu, 0);
		leayout.setExpandRatio(userView, 10);
		leayout.setExpandRatio(roleView, 10);
		this.setContent(leayout);	 
	}
	
	
}
