package unitn.dallatorre.resources;

import unitn.dallatorre.entities.Activity;
import unitn.dallatorre.entities.People;
import unitn.dallatorre.entities.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/*
 * TODO 
 * - There is a problem with the EntityManager injection through @PersistenceUnit or @PersistenceContext
 * - will look into it later
 */

@Path("/person")
public class PersonResources extends ResponseBuilder {

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET
	public Response hello() {
		People people = new People();
		people.readAllPersons();
		List<Person> personList = people.getPersons();
		if(personList.size()==0) {
			return throw404();
		}
		for (final Person person : personList) {
	          Activity latestActivity = person.getActivitypreference().get(person.getActivitypreference().size()-1);
	          latestActivity.setType(null);
	          person.getActivitypreference().clear();
	          person.getActivitypreference().add(latestActivity);
		}
		return throw200(people);
	}
	
	
	/*@POST
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response newPerson(Person person) throws IOException {
		System.out.println("Creating new person...");	
		Person p = Person.savePerson(person);
		if (p == null); //TODO
		return Response.status(Response.Status.CREATED).entity(p).build();
	}*/
	
	@GET
	@Path("{personId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getPerson(@PathParam("personId") int id) {		
		Person person = Person.getPersonById(id);
		if(person == null) {
			return throw404();
		}
		return throw200(person);
	}
	
	
}
