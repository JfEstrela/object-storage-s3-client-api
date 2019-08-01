package br.com.jfestrela.endpoint;

import static br.com.jfestrela.FileUtil.writeFile;

import java.io.File;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
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
	@Path("/bucket/file/{bucketName}/{objectName}")
	@RolesAllowed("ADM")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFile(@PathParam("bucketName") String bucketName,
			@PathParam("objectName") String objectName) throws OSS3Exception {
		return Response.ok(service.getFile(bucketName, objectName)).build();
	}
	
	@GET
	@Path("/bucket/download/{bucketName}/{objectName}")
	@RolesAllowed("ADM")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(@PathParam("bucketName") String bucketName,@PathParam("objectName") String objectName) throws OSS3Exception {
		File file = writeFile(service.getObjectOutputStream(bucketName, objectName).toByteArray(), objectName);
		return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
				      .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" )
				      .build();		
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
    @Path("/bucket/object-file/{bucketName}")
    @RolesAllowed("ADM")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response  createObject (@PathParam("bucketName") String bucketName , @MultipartForm FileS3 fileS3  ) throws OSS3Exception {
		return Response.ok(service.putFile(loadFile(bucketName, fileS3)) ).build();
    }

	private FileS3DTO loadFile(String bucketName, FileS3 fileS3) throws OSS3Exception {
		FileS3DTO fileDTO = new FileS3DTO();
		fileDTO.setBucketName(bucketName);
		fileDTO.setItemName(fileS3.getName());
		fileDTO.setFile(writeFile(fileS3.getData(), fileDTO.getItemName()));	
		return fileDTO;
	}
    

}
