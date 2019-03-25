package com.user.ui.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

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
import com.user.ui.model.User;
import com.user.ui.presenter.AddUserPresenter;
import com.user.ui.view.AddUserView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Notification.class)
public class AddUserPresenterTest {
	
	@InjectMocks
	private AddUserPresenter presenter;
	
	@Mock
	private UserRestClient restClient;
	@Mock
	private AddUserView view;
	
	@Captor
	private ArgumentCaptor<User> userArgumentCapture;
	
	@Captor
	private ArgumentCaptor<Collection<User>> usersArgumentCapture;
	
	@Before
	public void init() throws Exception {
		presenter.setView(view);
		
		PowerMockito.mockStatic(Notification.class);
    	PowerMockito.doNothing().when(Notification.class,"show",Mockito.anyString(), Mockito.eq(Type.TRAY_NOTIFICATION));
    	ArrayList<User> users = new ArrayList<User>();
    	
    	User user = new User();
		user.setId(1l);
		user.setUserName("ivan_5_5");
		user.setFirstName("Ivan");
		user.setLastName("Ivanov");
		user.setAge(44);
    	
		users.add(user);
		
    	Mockito.when(restClient.getAllJsonUsers()).thenReturn(users);
	}
	
	@Test
	public void addUserTest(){
		
		User user = new User();
		user.setId(1l);
		user.setUserName("ivan_5_5");
		user.setFirstName("Ivan");
		user.setLastName("Ivanov");
		user.setAge(44);
		presenter.addUser(user);
		
		Mockito.verify(restClient).createJsonUser(user);
		Mockito.verify(restClient).createJsonUser(userArgumentCapture.capture());
		
		Mockito.verify(restClient).getAllJsonUsers();
		Mockito.verify(view).updateParantViewData(restClient.getAllJsonUsers());
		Mockito.verify(view).updateParantViewData(usersArgumentCapture.capture());
		
		Mockito.verify(view).close();
		
		User captorUser = userArgumentCapture.getValue();
	    Collection<User> captorUsers = usersArgumentCapture.getValue();
		
		
		assertNotNull(captorUser);
		assertEquals(user.getId(), captorUser.getId());
		assertEquals(user.getUserName(), captorUser.getUserName());
		assertEquals(user.getFirstName(), captorUser.getFirstName());
		assertEquals(user.getLastName(), captorUser.getLastName());
		assertEquals(user.getAge(), captorUser.getAge());
		
		assertNotNull(captorUsers);
		assertTrue(captorUsers.size() != 0);
	}
	@Test
	public void editUserTest(){

    	
    	User user = new User();
    	user.setUserName("ivan_5_5");
		user.setFirstName("Ivan");
		user.setLastName("Ivanov");
		user.setAge(44);
		
        presenter.editUser(1l, user);
		
		Mockito.verify(restClient).updateJsonUser(user);
		Mockito.verify(restClient).updateJsonUser(userArgumentCapture.capture());
		
		Mockito.verify(restClient).getAllJsonUsers();
		Mockito.verify(view).updateParantViewData(restClient.getAllJsonUsers());
		Mockito.verify(view).updateParantViewData(usersArgumentCapture.capture());
		
		Mockito.verify(view).close();
		
		User captorUser = userArgumentCapture.getValue();
		Collection<User> captorUsers = usersArgumentCapture.getValue();
		
		assertNotNull(captorUser);
		assertEquals(user.getId(), captorUser.getId());
		assertEquals(user.getUserName(), captorUser.getUserName());
		assertEquals(user.getFirstName(), captorUser.getFirstName());
		assertEquals(user.getLastName(), captorUser.getLastName());
		assertEquals(user.getAge(), captorUser.getAge());
		
		assertNotNull(captorUsers);
		assertTrue(captorUsers.size() != 0);
    	
	}
	
}
