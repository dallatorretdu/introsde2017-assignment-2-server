package unitn.dallatorre.resources;

import unitn.dallatorre.entities.Activity;
import unitn.dallatorre.entities.ActivityType;
import unitn.dallatorre.entities.PeopleWrapper;
import unitn.dallatorre.entities.Person;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response hello() {
		PeopleWrapper people = new PeopleWrapper();
		people.readAllPersons();
		List<Person> personList = people.getPersons();
		if(personList.size()==0) {
			return throwNotFound404();
		}
		for (final Person person : personList) {
			if(person.getActivitypreference().size()>0) {
		        Activity latestActivity = person.getActivitypreference().get(person.getActivitypreference().size()-1);
		        latestActivity.setType(null);
		        person.getActivitypreference().clear();
		        person.getActivitypreference().add(latestActivity);
			}
		}
		return throwSuccess200(people);
	}
	
	@GET
	@Path("{personId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getPerson(@PathParam("personId") int id) {		
		Person person = Person.getPersonById(id);
		if(person == null) {
			return throwNotFound404();
		}
		return throwSuccess200(person);
	}
	
	@PUT
	@Path("{personId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updatePerson(@PathParam("personId") int id, Person person) throws IOException {
		if(id != person.getId()) {
			return throwBadRequest400("Given ID and new ID cannot be different");
		}
		Person databasePerson = Person.getPersonById(id);
		if (databasePerson == null) {
			return throwNotAcceptable406("Requested person not found");
		}
		
		databasePerson.setFirstname(person.getFirstname());
		databasePerson.setLastname(person.getLastname());
		databasePerson.setBirthdate(person.getBirthdate());
		databasePerson = Person.updatePerson(databasePerson);
		return throwSuccess200(databasePerson);
	}
	
	@POST
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response newPerson(Person person) throws IOException {	
		if (person.getId() != null) {
			return throwNotAcceptable406("cannot generate a person with a given ID");
		}
		if (person.getActivitypreference() != null) {
			List<Activity> activityPreference = person.getActivitypreference();
			for (Activity activity : activityPreference) {
				if(activity.getId() != null) {
					return throwNotAcceptable406("cannot generate an activity with a given ID");
				}
				if(activity.getType() == null) {
					return throwNotAcceptable406("an activity should have a type associated");
				}
				ActivityType activityType = ActivityType.getById(activity.getType().getType());
				if(activityType == null) {
					return throwNotAcceptable406("activity type not recognized");
				}
				activity.setType(activityType);
			}
		}
		Person p = Person.updatePerson(person);
		
		return throwCreated201(p);
	}
	
	@DELETE
	@Path("{personId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response deletePerson(@PathParam("personId") int id) {		
		Person person = Person.getPersonById(id);
		if(person == null) {
			return throwNotFound404();
		}
		if (person.getActivitypreference() != null) {
			List<Activity> activityPreference = person.getActivitypreference();
			for (Activity activity : activityPreference) {
				activity.setType(null);
			}
		}
		Person.removePerson(person);
		return throwNoContent204();
	}
	
}
