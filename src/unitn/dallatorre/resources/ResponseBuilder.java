package unitn.dallatorre.resources;

import javax.ws.rs.core.Response;

public class ResponseBuilder {

	public ResponseBuilder() {
		super();
	}

	protected Response throwNotFound404() {
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	protected Response throwSuccess200(Object o) {
		return Response.status(Response.Status.OK).entity(o).build();
	}
	
	protected Response throwNotAcceptable406(Object o) {
		return Response.status(Response.Status.NOT_ACCEPTABLE).entity(o).build();
	}
	
	protected Response throwBadRequest400(Object o) {
		return Response.status(Response.Status.NOT_ACCEPTABLE).entity(o).build();
	}

}