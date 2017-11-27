package unitn.dallatorre.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import unitn.dallatorre.entities.ActivityTypesWrapper;

@Path("/activity_types")
public class ActivityResources extends ResponseBuilder{
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getListOfActivityTypes() {
		ActivityTypesWrapper activityTypes = new ActivityTypesWrapper();
		activityTypes.readAllTypes();
		if (activityTypes.getActivityTypes() == null || activityTypes.getActivityTypes().size()==0) {
			return returnNotFound404();
		}
		return returnSuccess200(activityTypes);
	}
}
