package unitn.dallatorre.resources.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class HelpMeExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable e) {
        e.printStackTrace();
        if(e instanceof WebApplicationException ) {
        	return Response
                    .status(((WebApplicationException)e).getResponse().getStatus())
                    .type(MediaType.APPLICATION_JSON)
                    .entity(e.getCause())
                    .build();
		} 
        return Response
                    .status(Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(e.getCause())
                    .build();
    }

}