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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.user.ui.client.RoleRestClient;
import com.user.ui.model.Role;
import com.user.ui.presenter.RolePresenter;
import com.user.ui.view.AddRoleView;
import com.user.ui.view.RoleView;
import com.vaadin.ui.Notification;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Notification.class)
public class RolePresenterTest {
	@InjectMocks
	private RolePresenter presenter;
	
	@Mock
	private RoleView view;
	
	@Mock
	private RoleRestClient restClient;
	
	@Mock
	private AddRoleView addRoleView;
	
    @Captor
    private ArgumentCaptor<Role> roleArgumentCaptor;
    @Captor
    private ArgumentCaptor<Collection<Role>> rolesArgumentCaptor;
	
    Role role;
	@Before
	public void init() {
		presenter.setView(view);
		
		ArrayList<Role> roles = new ArrayList<Role>();
		
		role = new Role();
		role.setId(1l);
		role.setName("user");
		role.setDescription("user role");
		
		roles.add(role);
		
		Mockito.when(restClient.getJsonRole(Mockito.anyLong())).thenReturn(role);
		Mockito.when(restClient.getAllJsonRoles()).thenReturn(roles);
		
		
	}
	@Test
	public void getRolesTest() {
	    presenter.getRoles();
    	
    	Mockito.verify(restClient).getAllJsonRoles();
    	Mockito.verify(view).updateData(restClient.getAllJsonRoles());
    	Mockito.verify(view).updateData(rolesArgumentCaptor.capture());
    	
    	Collection<Role> rolesCapture = rolesArgumentCaptor.getValue();
    	
    	assertNotNull(rolesCapture);
    	
    	assertTrue(rolesCapture.size() != 0);
    	
	}
	@Test
	public void addRoleTest() {
		Mockito.when(addRoleView.isAttached()).thenReturn(true);
		presenter.addRole();

		Mockito.verify(addRoleView).setParentView(view);
		Mockito.verify(addRoleView).addRole();
	}
	
	@Test
	public void editRoleTest() {
		Mockito.when(addRoleView.isAttached()).thenReturn(true);
		Mockito.when(restClient.getJsonRole(Mockito.anyLong())).thenReturn(role);
		
		presenter.editRole(1l);
		
        Mockito.verify(restClient).getJsonRole(1l);
        
        Mockito.verify(addRoleView).editRole(role);
        Mockito.verify(addRoleView).editRole(roleArgumentCaptor.capture());
        
		Mockito.verify(addRoleView).setParentView(view);
		
		Role roleCapture = roleArgumentCaptor.getValue();
		
		assertNotNull(roleCapture);
		
		assertEquals(role.getId(), roleCapture.getId());
		assertEquals(role.getName(), roleCapture.getName());
		assertEquals(role.getDescription(), roleCapture.getDescription());
		
		
		
	}
	@Test
	public void deleteRoleTest() {
        presenter.deleteRole(1l);
    	
    	Mockito.verify(restClient).deleteJsonRole(1l);
    	
    	Mockito.verify(restClient).getAllJsonRoles();
    	
    	Mockito.verify(view).updateData(restClient.getAllJsonRoles());
        Mockito.verify(view).updateData(rolesArgumentCaptor.capture());
    	
    	Collection<Role> rolesCapture = rolesArgumentCaptor.getValue();
    	
    	assertNotNull(rolesCapture);
    	
    	assertTrue(rolesCapture.size() != 0);
	}
	@Test
	public void filterTest() {
		String name = "user";
		String description = "user role";
		
		presenter.filter(name, description);
		
		Mockito.verify(restClient).getAllJsonRoles();
    	Mockito.verify(view).updateData(restClient.getAllJsonRoles());
        Mockito.verify(view).updateData(rolesArgumentCaptor.capture());
    	
    	Collection<Role> rolesCapture = rolesArgumentCaptor.getValue();
    	
    	assertNotNull(rolesCapture);
    	
    	assertTrue(rolesCapture.size() != 0);
	}

}
