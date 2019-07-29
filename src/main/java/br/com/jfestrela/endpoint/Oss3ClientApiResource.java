package br.com.jfestrela.endpoint;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.Form;
import br.com.jfestrela.dto.FileS3DTO;
import br.com.jfestrela.exception.OSS3Exception;
import br.com.jfestrela.service.Oss3ClientApiService;

@Path("/oss3-client-api")
public class Oss3ClientApiResource {

	@Inject
	Oss3ClientApiService service;

	@GET
	@Path("/bucket")
	@RolesAllowed("READ")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBuckets() throws OSS3Exception {
		return Response.ok(service.listBuckets()).build();
	}

	@GET
	@Path("/bucket/objects/{bucketName}")
	@RolesAllowed("ADM")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getObjectList(@PathParam("bucketName") String bucketName) throws OSS3Exception {
		return Response.ok(service.listObjects(bucketName)).build();
	}

	@GET
	@Path("/bucket/object/{bucketName}/{objectName}")
	@RolesAllowed("ADM")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getObjectList(@PathParam("bucketName") String bucketName,
			@PathParam("objectName") String objectName) throws OSS3Exception {
		return Response.ok(service.getObjectOutputStream(bucketName, objectName)).build();
	}

	@POST
	@Path("/bucket")
	@RolesAllowed("ADM")
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String bucketName) throws OSS3Exception {
		return Response.ok(service.createBucket(bucketName)).build();
	}

	@POST
	@Path("/bucket/object-txt")
	@RolesAllowed("ADM")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createObjectText(FileS3DTO fileText) throws OSS3Exception {
		return Response.ok(service.putTextFile(fileText)).build();
	}

	@POST
    @Path("/bucket/object-file")
    @RolesAllowed("ADM")
    @Produces(MediaType.APPLICATION_JSON)
    public Response  createObject (@Form FileS3DTO fileS3) throws OSS3Exception {
        return Response.ok(service.putFile(fileS3) ).build();
    }
    

}
