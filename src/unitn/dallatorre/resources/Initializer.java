package unitn.dallatorre.resources;

import unitn.dallatorre.entities.Activity;
import unitn.dallatorre.entities.ActivityType;
import unitn.dallatorre.entities.PeopleWrapper;
import unitn.dallatorre.entities.Person;
import unitn.dallatorre.resources.exception.HelpMeExceptionMapper;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import javax.ws.rs.ext.Provider;

/*
 * TODO 
 * - There is a problem with the EntityManager injection through @PersistenceUnit or @PersistenceContext
 * - will look into it later
 */

@Path("/database_init")
public class Initializer extends ResponseBuilder {

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response hello() {
		
		ActivityType t1 = generateAndSaveActivityType("Sport");
		ActivityType t2 = generateAndSaveActivityType("Social");
		ActivityType t3 = generateAndSaveActivityType("Dota");
		
		Activity a1 = generateAndSaveActivity(t1, "Moving on your own power", "Running", "Albere");
		Activity a2 = generateAndSaveActivity(t1, "Going on 2 wheels", "Cycling", "Lung  Adige");
		Activity a3 = generateAndSaveActivity(t2, "Moving randomly", "Dancing", "Papafico");
		Activity a4 = generateAndSaveActivity(t2, "Getting drunk", "Drinking", "H-Acca");
		Activity a5 = generateAndSaveActivity(t3, "DOTA is Life", "DOTA", "col Tonno");

		Person p1 = generateAndSavePerson(a1, "Mario", "Rossi");
		Person p2 = generateAndSavePerson(a2, "Giulio", "Dallatorre");
		Person p3 = generateAndSavePerson(a3, "Stefano", "Tavonatti");
		Person p4 = generateAndSavePerson(a4, "Pinco", "Pallino");
		Person p5 = generateAndSavePerson(a5, "Manuel", "Dezulian");
		
		List<Person> personList = new ArrayList<Person>();
		personList.add(p1);
		personList.add(p2);
		personList.add(p3);
		personList.add(p4);
		personList.add(p5);
		PeopleWrapper people = new PeopleWrapper();
		people.setPersons(personList);
		
		return throwSuccess200(people);
		
	}
	
	private Person generateAndSavePerson(Activity activity, String firstname, String lastname) {
		Person p = new Person();
		p.setFirstname(firstname);
		p.setLastname(lastname);
		p.setBirthdate(new Date(System.currentTimeMillis()));
		List<Activity> activitypreference = new ArrayList<Activity>();
		activitypreference.add(activity);
		p.setActivitypreference(activitypreference);
		return Person.updatePerson(p);
	}

	private Activity generateAndSaveActivity(ActivityType activityType, String description, String name, String place) {
		Activity a = new Activity();
		a.setDescription(description);
		a.setName(name);
		a.setPlace(place);
		a.setStartdate(new Date(System.currentTimeMillis()));
		a.setType(activityType);
		a=Activity.updateActivity(a);
		return a;
	}

	private ActivityType generateAndSaveActivityType(String activityTypeName) {
		ActivityType t1 = new ActivityType();
		t1.setType(activityTypeName);
		t1=ActivityType.updateActivityType(t1);
		return t1;
	}
}
