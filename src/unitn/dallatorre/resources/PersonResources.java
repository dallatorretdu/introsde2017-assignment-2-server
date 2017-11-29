package unitn.dallatorre.resources;

import unitn.dallatorre.entities.Activity;
import unitn.dallatorre.entities.ActivityType;
import unitn.dallatorre.entities.ActivityWrapper;
import unitn.dallatorre.entities.PeopleWrapper;
import unitn.dallatorre.entities.Person;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
			return returnNotFound404();
		}
		for (final Person person : personList) {
			if(person.getActivitypreference().size()>0) {
		        Activity latestActivity = person.getActivitypreference().get(person.getActivitypreference().size()-1);
		        latestActivity.setType(null);
		        person.getActivitypreference().clear();
		        person.getActivitypreference().add(latestActivity);
			}
		}
		return returnSuccess200(people);
	}
	
	@GET
	@Path("{personId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getPerson(@PathParam("personId") int id) {		
		Person person = Person.getPersonById(id);
		if(person == null) {
			return returnNotFound404();
		}
		return returnSuccess200(person);
	}
	
	@GET
	@Path("{personId}/{activity_type}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getPersonActivityWithDate(@QueryParam("before") String endDate, @QueryParam("after") String beginDate, @PathParam("personId") int id, @PathParam("activity_type") String givenActivityType) {		
		Person person = Person.getPersonById(id);
		ActivityType activityType = ActivityType.getById(givenActivityType);
		
		if(person == null || activityType == null) {
			return returnNotFound404();
		}
		
		ActivityWrapper activities = new ActivityWrapper();
		activities.setActivity(person.getActivitypreference());
		activities.filterActivities(activityType);
		if( beginDate != null && endDate != null) {
			activities.filterActivities(parseDate(beginDate),parseDate(endDate));
		}
		if(activities.getActivity().size() == 0) {
			return returnNotFound404();
		}
		return returnSuccess200(activities);
	}
	
	@GET
	@Path("{personId}/{activity_type}/{activity_id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getPersonActivity(@PathParam("personId") int id, @PathParam("activity_type") String givenActivityType, @PathParam("activity_id") int activityId) {		
		Person person = Person.getPersonById(id);
		ActivityType activityType = ActivityType.getById(givenActivityType);
		
		if(person == null || activityType == null) {
			return returnNotFound404();
		}
		
		ActivityWrapper activities = new ActivityWrapper();
		activities.setActivity(person.getActivitypreference());
		activities.filterActivities(activityType);
		activities.filterActivities(activityId);
		
		return returnSuccess200(activities);
	}
	
	@PUT
	@Path("{personId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updatePerson(@PathParam("personId") int id, Person person) throws IOException {
		if(id != person.getId()) {
			return returnBadRequest400("Given ID and new ID cannot be different");
		}
		Person databasePerson = Person.getPersonById(id);
		if (databasePerson == null) {
			return returnNotAcceptable406("Requested person not found");
		}
		
		databasePerson.setFirstname(person.getFirstname());
		databasePerson.setLastname(person.getLastname());
		databasePerson.setBirthdate(person.getBirthdate());
		databasePerson = Person.updatePerson(databasePerson);
		return returnSuccess200(databasePerson);
	}
	
	@PUT
	@Path("{personId}/{activity_type}/{activity_id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updateActivity(@PathParam("personId") int id, @PathParam("activity_type") String givenActivityType, @PathParam("activity_id") int activityId) {		
		Person person = Person.getPersonById(id);
		ActivityType activityType = ActivityType.getById(givenActivityType);
		Activity activity = Activity.getActivityById(activityId);
		
		if(person == null || activityType == null || activity == null) {
			return returnNotFound404();
		}
		if(!person.getActivitypreference().contains(activity)) {
			return returnNotAcceptable406("Requested person does not relate to Requested activity");
		}
		
		person.getActivitypreference().get( person.getActivitypreference().indexOf(activity) ).setType(activityType);
		person = Person.updatePerson(person);
		
		return returnSuccess200(activity);
	}
	
	@POST
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response newPerson(Person person) throws IOException {	
		if (person.getId() != null) {
			return returnNotAcceptable406("cannot generate a person with a given ID");
		}
		if (person.getActivitypreference() != null) {
			List<Activity> activityPreference = person.getActivitypreference();
			for (Activity activity : activityPreference) {
				if(activity.getId() != null) {
					return returnNotAcceptable406("cannot generate an activity with a given ID");
				}
				if(activity.getType() == null) {
					return returnNotAcceptable406("an activity should have a type associated");
				}
				ActivityType activityType = ActivityType.getById(activity.getType().getType());
				if(activityType == null) {
					return returnNotAcceptable406("activity type not recognized");
				}
				activity.setType(activityType);
			}
		}
		Person p = Person.updatePerson(person);
		
		return returnCreated201(p);
	}
	
	@POST
	@Path("{personId}/{activity_type}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response postPersonActivity(Activity inputActivity, @PathParam("personId") int id, @PathParam("activity_type") String givenActivityType) {		
		Person person = Person.getPersonById(id);
		ActivityType activityType = ActivityType.getById(givenActivityType);
		
		if(person == null || activityType == null) {
			return returnNotAcceptable406("Cannot generate a new activity for an unexisting person or type");
		}
		if(inputActivity.getId() != null) {
			return returnNotAcceptable406("Cannot generate a new activity with a given ID");
		}
		if(inputActivity.getType() != null && !inputActivity.getType().getType().equals(givenActivityType)) {
			return returnNotAcceptable406("Incongruency between given Type and Type in body");
		}
		Activity newActivity = new Activity();
		newActivity.setType(activityType);
		newActivity.setName(inputActivity.getName());
		newActivity.setPlace(inputActivity.getPlace());
		newActivity.setStartdate(inputActivity.getStartdate());
		
		person.getActivitypreference().add(newActivity);
		person = Person.updatePerson(person);
		
		ActivityWrapper activities = new ActivityWrapper();
		activities.setActivity(person.getActivitypreference());
		
		return returnCreated201(activities);
	}
	
	@DELETE
	@Path("{personId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response deletePerson(@PathParam("personId") int id) {		
		Person person = Person.getPersonById(id);
		if(person == null) {
			return returnNotFound404();
		}
		if (person.getActivitypreference() != null) {
			List<Activity> activityPreference = person.getActivitypreference();
			for (Activity activity : activityPreference) {
				activity.setType(null);
			}
		}
		Person.removePerson(person);
		return returnNoContent204();
	}
	
	public static Date parseDate(String date) {
	     try {
	         return new SimpleDateFormat("yyyy-MM-dd").parse(date);
	     } catch (ParseException e) {
	         return null;
	     }
	  }
}
