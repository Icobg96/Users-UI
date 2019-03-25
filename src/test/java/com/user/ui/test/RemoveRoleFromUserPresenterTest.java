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

import com.user.ui.client.UserRestClient;
import com.user.ui.model.Role;
import com.user.ui.model.User;
import com.user.ui.presenter.RemoveRoleFromUserPresenter;
import com.user.ui.view.RemoveRoleFromUserView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Notification.class)
public class RemoveRoleFromUserPresenterTest {
	
	@InjectMocks
	private RemoveRoleFromUserPresenter presenter;
	
	@Mock
	private UserRestClient userClient;
	@Mock
	private RemoveRoleFromUserView view;
	
	@Captor
	private ArgumentCaptor<User> userArgumentCaptor;
	@Captor
	private ArgumentCaptor<Collection<User>> usersArgumentCaptor;
	
	private User user;
	
	@Before
	public void init() throws Exception {
		presenter.setView(view);
		
		user = new User();
		
		ArrayList<User> users = new ArrayList<User>();
		users.add(user);
	
		Mockito.when(userClient.getAllJsonUsers()).thenReturn(users);

		PowerMockito.mockStatic(Notification.class);
    	PowerMockito.doNothing().when(Notification.class,"show",Mockito.anyString(), Mockito.eq(Type.TRAY_NOTIFICATION));
	}
	@Test
	public void removeRoleToUserTest() {
		User user = new User();
		Role role = new Role();
		user.setRoles(new HashSet<Role>());
		user.getRoles().add(role);
		
		presenter.removeRoleToUser(user, role);
		
		Mockito.verify(userClient).updateJsonUserRoles(user);
		Mockito.verify(userClient).updateJsonUserRoles(userArgumentCaptor.capture());
		
		Mockito.verify(userClient).getAllJsonUsers();
		Mockito.verify(view).updateUserView(userClient.getAllJsonUsers());
		Mockito.verify(view).updateUserView(usersArgumentCaptor.capture());
		
		Mockito.verify(view).close();
		
        User userCaptor = userArgumentCaptor.getValue();
		
		Collection<User> usersCaptor = usersArgumentCaptor.getValue();
		
		assertNotNull(userCaptor);
		
		assertTrue(userCaptor.getRoles().size() == 0);
		
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
	
		Mockito.verify(view).updateRoleComboBox();
	}

}
