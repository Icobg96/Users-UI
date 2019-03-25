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

import com.user.ui.model.User;

@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserRestClient {
	    
	    @Value("${url.property.users}")
	    private String restUrl;
	    private Client client = ClientBuilder.newClient();

	    public Response createJsonUser(User user) {
	        return client.target(restUrl).request(MediaType.APPLICATION_JSON).post(Entity.entity(user, MediaType.APPLICATION_JSON));
	    }
	    public Response updateJsonUser(User user) {
	        return client.target(restUrl).request(MediaType.APPLICATION_JSON).put(Entity.entity(user, MediaType.APPLICATION_JSON));
	    }
	    public Response deleteJsonUser(Long id) {
	        return client.target(restUrl).path(String.valueOf(id)).request(MediaType.APPLICATION_JSON).delete();
	    }

	    public User getJsonUser(Long id) {
	        return client.target(restUrl).path(String.valueOf(id)).request(MediaType.APPLICATION_JSON).get(User.class);
	    }
	    public Collection<User> getAllJsonUsers(){
	    	return client.target(restUrl).request(MediaType.APPLICATION_JSON).get(new GenericType<Collection<User>>(){});
	    }
	    public Response updateJsonUserRoles(User user) {
	    	return client.target(restUrl+"/update/role").request(MediaType.APPLICATION_JSON).put(Entity.entity(user, MediaType.APPLICATION_JSON));
	    }
}
