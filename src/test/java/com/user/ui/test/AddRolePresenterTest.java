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

import com.user.ui.client.RoleRestClient;
import com.user.ui.model.Role;
import com.user.ui.presenter.AddRolePresenter;
import com.user.ui.view.AddRoleView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Notification.class)
public class AddRolePresenterTest {
	@InjectMocks
	private AddRolePresenter presenter;
	
	@Mock
	private RoleRestClient restClient;
	@Mock
	private AddRoleView view;
	
	@Captor
	private ArgumentCaptor<Role> roleArgumentCaptor;
	@Captor
	private ArgumentCaptor<Collection<Role>> rolesArgumentCaptor;
	
	@Before
	public void init() throws Exception {
		presenter.setView(view);
		
		PowerMockito.mockStatic(Notification.class);
    	PowerMockito.doNothing().when(Notification.class,"show",Mockito.anyString(), Mockito.eq(Type.TRAY_NOTIFICATION));
    	
    	ArrayList<Role> roles = new ArrayList<Role>();
    	
    	Role role = new Role();
		role.setId(1l);
		role.setName("user");
		role.setDescription("user role");
		roles.add(role);
		
		Mockito.when(restClient.getAllJsonRoles()).thenReturn(roles);
		
	}
	
	@Test
	public void addRoleTest(){
    	
		Role role = new Role();
		role.setId(1l);
		role.setName("user");
		role.setDescription("user role");
		
		presenter.addRole(role);
		
		Mockito.verify(restClient).createJsonRole(role);
		Mockito.verify(restClient).createJsonRole(roleArgumentCaptor.capture());
		
		Mockito.verify(restClient).getAllJsonRoles();
		Mockito.verify(view).updateParantViewData(restClient.getAllJsonRoles());
		Mockito.verify(view).updateParantViewData(rolesArgumentCaptor.capture());
		
		Mockito.verify(view).close();
		
		Role captureRole = roleArgumentCaptor.getValue();
		Collection<Role> captoreRoles = rolesArgumentCaptor.getValue();
		
		assertNotNull(captureRole);
		
		assertEquals(role.getId(), captureRole.getId());
		assertEquals(role.getName(), captureRole.getName());
		assertEquals(role.getDescription(), captureRole.getDescription());
		
		assertNotNull(captoreRoles);
		
		assertTrue(captoreRoles.size() != 0);

	}
	@Test
	public void editRoleTest(){

		Role role = new Role();
		role.setName("user");
		role.setDescription("user role");
		role.setName("user");
		presenter.editRole(1l,role);
		
		Mockito.verify(restClient).updateJsonRole(role);
		Mockito.verify(restClient).updateJsonRole(roleArgumentCaptor.capture());
		Mockito.verify(restClient).getAllJsonRoles();
		
		Mockito.verify(view).updateParantViewData(restClient.getAllJsonRoles());
		Mockito.verify(view).updateParantViewData(rolesArgumentCaptor.capture());
		
		Mockito.verify(view).close();
		
		Role captureRole = roleArgumentCaptor.getValue();
		Collection<Role> captoreRoles = rolesArgumentCaptor.getValue();
		
		assertNotNull(captureRole);
		
		assertEquals(role.getId(), captureRole.getId());
		assertEquals(role.getName(), captureRole.getName());
		assertEquals(role.getDescription(), captureRole.getDescription());
		
		assertNotNull(captoreRoles);
		
		assertTrue(captoreRoles.size() != 0);
    	
	}
}
