package com.user.ui.client;

import java.util.Collection;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.user.ui.model.Role;

@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RoleRestClient {
	  @Value("${url.property.roles}")
	  private String restUrl;
	  private Client client = ClientBuilder.newClient();
	  
	  public Response createJsonRole(Role role) {
	        return client.target(restUrl).request(MediaType.APPLICATION_JSON).post(Entity.entity(role, MediaType.APPLICATION_JSON));
	    }
	    public Response updateJsonRole(Role role) {
	        return client.target(restUrl).request(MediaType.APPLICATION_JSON).put(Entity.entity(role, MediaType.APPLICATION_JSON));
	    }
	    public Response deleteJsonRole(Long id) {
	        return client.target(restUrl).path(String.valueOf(id)).request(MediaType.APPLICATION_JSON).delete();
	    }

	    public Role getJsonRole(Long id) {
	        return client.target(restUrl).path(String.valueOf(id)).request(MediaType.APPLICATION_JSON).get(Role.class);
	    }
	    public Collection<Role> getAllJsonRoles(){
	    	return client.target(restUrl).request(MediaType.APPLICATION_JSON).get(new GenericType<Collection<Role>>(){});
	    }
	  

	  
}
