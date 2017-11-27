package unitn.dallatorre.resources;

import javax.ws.rs.core.Response;

public class ResponseBuilder {

	public ResponseBuilder() {
		super();
	}

	protected Response returnSuccess200(Object o) {
		return Response.status(Response.Status.OK).entity(o).build();
	}

	protected Response returnCreated201(Object o) {
		return Response.status(Response.Status.CREATED).entity(o).build();
	}

	protected Response returnNoContent204() {
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	protected Response returnNotFound404() {
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	protected Response returnNotAcceptable406(Object o) {
		return Response.status(Response.Status.NOT_ACCEPTABLE).entity(o).build();
	}
	
	protected Response returnBadRequest400(Object o) {
		return Response.status(Response.Status.BAD_REQUEST).entity(o).build();
	}

}