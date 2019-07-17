package br.com.jfestrela.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectMetadata;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectResult;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;

import br.com.jfestrela.config.OSS3Config;
import br.com.jfestrela.dto.FileS3DTO;

@ApplicationScoped
public class Oss3ClientApiService {

private static final Logger LOG = LoggerFactory.getLogger(Oss3ClientApiService.class);
	
	private AmazonS3 cosClient;
	@Inject
	private OSS3Config oss3Config;
	

    /**
     * @param bucketName
     * @param clientNum
     * @param api_key
     * @param service_instance_id
     * @param endpoint_url
     * @param location
     * @return AmazonS3
     */
    private  AmazonS3 createClient(){
    	if(cosClient == null) {
	        AWSCredentials credentials;
	        credentials = new BasicIBMOAuthCredentials(oss3Config.getApi_key(), oss3Config.getService_instance_id());
	
	        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
	        clientConfig.setUseTcpKeepAlive(true);
	
	        cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
	                .withEndpointConfiguration(new EndpointConfiguration(oss3Config.getEndpoint_url(), oss3Config.getLocation())).withPathStyleAccessEnabled(true)
	                .withClientConfiguration(clientConfig).build();
    	}
    	return cosClient;

    }

    /**
     * @param bucketName
     */
    public  ObjectListing listObjects(String bucketName){	
    	LOG.warn("Listing objects in bucket " + bucketName);
        ObjectListing objectListing = this.createClient().listObjects(new ListObjectsRequest().withBucketName(bucketName));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
        	LOG.info(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
        }
        return objectListing;
    }
    
    public  ObjectListing listObjectsDefault(){	
        return listObjects(oss3Config.getBucketName());
    }

    /**
     * @param bucketName
     * @param cosClient
     * @param storageClass
     */
    @SuppressWarnings("deprecation")
	public  Bucket createBucket(String bucketName ){
    	return this.createClient().createBucket(bucketName, oss3Config.getStorageClass());
    }
    
   	public  Bucket createBucketDefault( ){
       	return createBucket(oss3Config.getBucketName());
    }

    /**
     * @param cosClient
     */
    public  List<Bucket> listBuckets(){
    	LOG.warn("Listing buckets");
        final List<Bucket> bucketList = this.createClient().listBuckets();
        for (final Bucket bucket : bucketList) {
        	LOG.info(bucket.getName());
        }
        return bucketList;
    }
    
    public PutObjectResult createTextFile(FileS3DTO fileText) {
        LOG.warn("Creating new item: "+fileText.getItemName());
        InputStream newStream = new ByteArrayInputStream(fileText.getFileText().getBytes(StandardCharsets.UTF_8));

        ObjectMetadata metadata = new ObjectMetadata();        
        metadata.setContentLength(fileText.getFileText().length());

        PutObjectRequest req = new PutObjectRequest(fileText.getBucketName(), fileText.getItemName(), newStream, metadata);
        PutObjectResult objectResult =  this.createClient().putObject(req);
        LOG.warn("Item: "+fileText.getItemName()+" created!");
        return objectResult;
    }
    
    public PutObjectResult createFile(FileS3DTO fileS3) {
        LOG.warn("Creating new item: "+fileS3.getItemName()); 
        PutObjectResult objectResult =  this.createClient().putObject(
        		fileS3.getBucketName(), // the name of the destination bucket
        	    fileS3.getItemName(), // the object key
        	    fileS3.getFile() // the file name and path of the object to be uploaded
        	);
        LOG.warn("Item: "+fileS3.getItemName()+" created!");
        return objectResult;
    }


}