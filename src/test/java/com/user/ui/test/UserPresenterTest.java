package com.user.ui.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.user.ui.presenter.UserPresenter;
import com.user.ui.view.AddUserView;
import com.user.ui.view.UserView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PrepareForTest({Notification.class,UI.class})
public class UserPresenterTest {
	
    @InjectMocks
	private UserPresenter presenter;
    
    @Mock
    private UserView view;
    
    @Mock
    private UserRestClient restClient;
    
    @Mock
    private AddUserView addUserView;
   
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @Captor
    private ArgumentCaptor<Collection<User>> usersArgumentCaptor;
    
    
    User user;
    @Before
    public void init() {
    	presenter.setView(view);
    	ArrayList<User> users = new ArrayList<User>();
    	
    	user = new User();
    	user.setId(1l);
		user.setUserName("ivan_4_5");
		user.setFirstName("Ivan");
		user.setLastName("Ivanov");
		user.setAge(44);
		
		users.add(user);
		
    	Mockito.when(restClient.getJsonUser(Mockito.anyLong())).thenReturn(user);
    	Mockito.when(restClient.getAllJsonUsers()).thenReturn(users);
    }
    
    @Test
    public void getUserTest() {
    	presenter.getUsers();
    	
    	Mockito.verify(restClient).getAllJsonUsers();
    	Mockito.verify(view).updateData(restClient.getAllJsonUsers());
    	Mockito.verify(view).updateData(usersArgumentCaptor.capture());
    	
    	Collection<User> usersCaptor = usersArgumentCaptor.getValue();
    	
    	assertNotNull(usersCaptor);
    	assertTrue(usersCaptor.size() != 0);
    }
    @Test
    public void addUserTest() {
    	Mockito.when(addUserView.isAttached()).thenReturn(true);
    	presenter.addUser();
    	
    	Mockito.verify(addUserView).addUser();
    	Mockito.verify(addUserView).setParenView(view);
    }
    @Test
    public void editUserTest() {

    	Mockito.when(addUserView.isAttached()).thenReturn(true);
    	presenter.editUser(1l);

    	Mockito.verify(restClient).getJsonUser(1l);
    	
    	Mockito.verify(addUserView).editUser(user);
    	Mockito.verify(addUserView).editUser(userArgumentCaptor.capture());
    	
    	Mockito.verify(addUserView).setParenView(view);
    	
    	User userCaptor = userArgumentCaptor.getValue();
    	
    	assertNotNull(userCaptor);
    	
    	assertEquals(user.getId(), userCaptor.getId());
    	assertEquals(user.getUserName(), userCaptor.getUserName());
    	assertEquals(user.getFirstName(), userCaptor.getFirstName());
    	assertEquals(user.getLastName(), userCaptor.getLastName());
    	assertEquals(user.getAge(), userCaptor.getAge());
    	
    }
    @Test
    public void deleteUserTest() {
    	
    	presenter.deleteUser(1l);
    	
    	Mockito.verify(restClient).deleteJsonUser(1l);
    	Mockito.verify(restClient).getAllJsonUsers();
    	Mockito.verify(view).updateData(restClient.getAllJsonUsers());
        Mockito.verify(view).updateData(usersArgumentCaptor.capture());
    	
    	Collection<User> usersCaptor = usersArgumentCaptor.getValue();
    	
    	assertNotNull(usersCaptor);
    	assertTrue(usersCaptor.size() != 0);
    }
    @Test
    public void filterWithAgeTest() {
    	String userName = "ivan_4_5";
    	String firstName = "Ivan";
    	String lastName = "Ivanov";
    	String age = "44";
    	
    	presenter.filter(userName, firstName, lastName, age);
    	
    	Mockito.verify(view).updateData(restClient.getAllJsonUsers());
        Mockito.verify(view).updateData(usersArgumentCaptor.capture());
    	
    	Collection<User> usersCaptor = usersArgumentCaptor.getValue();
    	
    	assertNotNull(usersCaptor);
    	assertTrue(usersCaptor.size() != 0);
    }
    @Test
    public void filterWithOutAgeTest() {
    	String userName = "ivan_4_5";
    	String firstName = "Ivan";
    	String lastName = "Ivanov";

    	
    	presenter.filter(userName, firstName, lastName, null);
    	
    	Mockito.verify(view).updateData(restClient.getAllJsonUsers());
        Mockito.verify(view).updateData(usersArgumentCaptor.capture());
    	
    	Collection<User> usersCaptor = usersArgumentCaptor.getValue();
    	
    	assertNotNull(usersCaptor);
    	assertTrue(usersCaptor.size() != 0);
    }
    @Test
    public void deleteRoleFromUserTest() throws Exception{
    	PowerMockito.mockStatic(Notification.class);
    	PowerMockito.doNothing().when(Notification.class,"show",Mockito.anyString(), Mockito.eq(Type.TRAY_NOTIFICATION));
        
    	Role role = new Role();
    	role.setId(1l);
    	role.setName("user");
    	role.setDescription("user role");
    	user.setRoles(new HashSet<Role>());
    	user.getRoles().add(role);
    	
        presenter.deleteRoleFromUser(role, user);
    	
    	Mockito.verify(restClient).updateJsonUserRoles(user);
    	Mockito.verify(restClient).updateJsonUserRoles(userArgumentCaptor.capture());
    	
       User userCapture = userArgumentCaptor.getValue();
       
       assertNotNull(userCapture);
       assertTrue(userCapture.getRoles().size() == 0);
    	
    }
}
