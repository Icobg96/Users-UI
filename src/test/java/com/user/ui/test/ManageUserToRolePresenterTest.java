package com.user.ui.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.user.ui.client.RoleRestClient;
import com.user.ui.client.UserRestClient;
import com.user.ui.model.Role;
import com.user.ui.model.User;
import com.user.ui.presenter.ManageUserToRolePresenter;
import com.user.ui.view.ManageUserToRoleView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Notification.class)
public class ManageUserToRolePresenterTest {
	
	@InjectMocks
	private ManageUserToRolePresenter presenter;
	
	@Mock
	private RoleRestClient roleClient;
	@Mock
	private UserRestClient userClient;
	@Mock
	private ManageUserToRoleView view;
	
	@Captor
	private ArgumentCaptor<User> userArgumentCaptor;
	@Captor
	private ArgumentCaptor<Collection<User>> usersArgumentCaptor;
	@Captor
	private ArgumentCaptor<Collection<Role>> rolesArgumentCaptor;
	
	private User user;
	@Before
	public void init() throws Exception {
		presenter.setView(view);
		
		user = new User();
		Role role = new Role();
		
		ArrayList<User> users = new ArrayList<User>();
		users.add(user);
		
		ArrayList<Role> roles = new ArrayList<Role>();
		roles.add(role);
		
		Mockito.when(userClient.getAllJsonUsers()).thenReturn(users);
		Mockito.when(roleClient.getAllJsonRoles()).thenReturn(roles);
		
		PowerMockito.mockStatic(Notification.class);
    	PowerMockito.doNothing().when(Notification.class,"show",Mockito.anyString(), Mockito.eq(Type.TRAY_NOTIFICATION));
	}
	
	@Test
	public void addRoleToUserTest() {
		Role role = new Role();
		
		user.setRoles(new HashSet<Role>());
		presenter.addRoleToUser(user, role);
		
		Mockito.verify(userClient).updateJsonUserRoles(user);
		Mockito.verify(userClient).updateJsonUserRoles(userArgumentCaptor.capture());
		
		Mockito.verify(userClient).getAllJsonUsers();
		Mockito.verify(view).updateUserView(userClient.getAllJsonUsers());
		Mockito.verify(view).updateUserView(usersArgumentCaptor.capture());
		
		Mockito.verify(view).close();
		
		User userCaptor = userArgumentCaptor.getValue();
		
		Collection<User> usersCaptor = usersArgumentCaptor.getValue();
		
		assertNotNull(userCaptor);
		
		assertTrue(userCaptor.getRoles().size() > 0);
		
		assertNotNull(usersCaptor);
    	assertTrue(usersCaptor.size() != 0);
		
	}
	
	@Test
	public void getUsersTest() {
		presenter.getUsers();
		
		Mockito.verify(userClient).getAllJsonUsers();
		
		Mockito.verify(view).updateUserComboBox(userClient.getAllJsonUsers());
		Mockito.verify(view).updateUserComboBox(usersArgumentCaptor.capture());
		
		Collection<User> usersCaptor = usersArgumentCaptor.getValue();
		
		assertNotNull(usersCaptor);
		
    	assertTrue(usersCaptor.size() > 0);
	}
	
	@Test
	public void getRolesTest() {
		presenter.getRoles();
		
		Mockito.verify(roleClient).getAllJsonRoles();
		Mockito.verify(view).updateRoleComboBox(roleClient.getAllJsonRoles());
        Mockito.verify(view).updateRoleComboBox(rolesArgumentCaptor.capture());
		
		Collection<Role> rolesCaptor = rolesArgumentCaptor.getValue();
		
		assertNotNull(rolesCaptor);
		
    	assertTrue(rolesCaptor.size() > 0);
	}

}
