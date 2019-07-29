package br.com.jfestrela.exception;

import javax.json.Json;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ibm.cloud.objectstorage.services.s3.model.AmazonS3Exception;

@Provider
public class ExceptionsHandler implements ExceptionMapper<Throwable>{

	@Override
	public Response toResponse(Throwable exception) {	
		int code = 500;
		if(exception instanceof AmazonS3Exception) {
			 code = ((AmazonS3Exception) exception).getStatusCode();
		}else if(exception instanceof OSS3Exception) {
			return verifyExceptionApi(exception);
		}		
		return Response.status(code)
		            .entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build())
		            .build();
	}

	private Response verifyExceptionApi(Throwable exception ) {
		int code = 500;
		if(exception.getCause() == null) {
			code = 200;
		}else {
			 return Response.status(code)
			                .entity(Json.createObjectBuilder()
			                .add("error", exception.getMessage()).add("code", code)
			            	.add("case", exception.getCause().getMessage()).build())
			                .build();
		}
		return getResponseDefault(code, exception);
	}
	
	private Response getResponseDefault(int code,Throwable exception) {
		 return Response.status(code)
		            .entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build())
		            .build();
	}

}
