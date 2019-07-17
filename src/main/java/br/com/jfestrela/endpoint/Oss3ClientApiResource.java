package br.com.jfestrela.endpoint;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.jfestrela.dto.FileS3DTO;
import br.com.jfestrela.service.Oss3ClientApiService;

@Path("/oss3-client-api")
public class Oss3ClientApiResource {

    @Inject
    Oss3ClientApiService service;

    @GET
    @Path("/buckets")
    @Produces(MediaType.APPLICATION_JSON)
    public  Response getBuckets() {
        return Response.ok(service.listBuckets()).build() ;
    }
    
    @GET
    @Path("/buckets/objects/{bucketName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response  getObjectList(@PathParam("bucketName") String bucketName) {
        return Response.ok(service.listObjects(bucketName)).build();
    }
    
    @POST
    @Path("/buckets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response  create(String bucketName) {
        return Response.ok(service.createBucket(bucketName)).build();
    }

    @GET
    @Path("/default/buckets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response  getObjectListsDefault() {
        return Response.ok(service.listObjectsDefault()).build();
    }
    
    @POST
    @Path("/buckets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response  createDefault(String bucketName) {
        return Response.ok(service.createBucketDefault()).build();
    }
    
    @POST
    @Path("/buckets/objects-txt")
    @Produces(MediaType.APPLICATION_JSON)
    public Response  createObjectText(FileS3DTO fileText) {
        return Response.ok(service.createTextFile(fileText) ).build();
    }
    

}