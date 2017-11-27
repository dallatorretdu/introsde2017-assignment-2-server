package unitn.dallatorre.resources;

import javax.ws.rs.core.Response;

public class ResponseBuilder {

	public ResponseBuilder() {
		super();
	}

	protected Response throw404() {
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	protected Response throw200(Object o) {
		return Response.status(Response.Status.OK).entity(o).build();
	}

}