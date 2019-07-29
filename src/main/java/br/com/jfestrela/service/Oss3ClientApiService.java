package br.com.jfestrela.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SdkClientException;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.AbortMultipartUploadRequest;
import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
import com.ibm.cloud.objectstorage.services.s3.model.CompleteMultipartUploadRequest;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadRequest;
import com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadResult;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectMetadata;
import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectResult;
import com.ibm.cloud.objectstorage.services.s3.model.S3Object;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectInputStream;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
import com.ibm.cloud.objectstorage.services.s3.model.UploadPartRequest;
import com.ibm.cloud.objectstorage.services.s3.model.UploadPartResult;

import br.com.jfestrela.config.OSS3Config;
import br.com.jfestrela.dto.FileS3DTO;
import br.com.jfestrela.exception.OSS3Exception;

@ApplicationScoped
public class Oss3ClientApiService {

private static final Logger LOG = LoggerFactory.getLogger(Oss3ClientApiService.class);
	
	private AmazonS3 cosClient;
	@Inject
	OSS3Config oss3Config;
	

    /**
     * @param bucketName
     * @param clientNum
     * @param api_key
     * @param service_instance_id
     * @param endpoint_url
     * @param location
     * @return AmazonS3
     * @throws OSS3Exception 
     */
    public  AmazonS3 createClient() {
    	if(cosClient == null) {
		    AWSCredentials credentials;
	        credentials = new BasicIBMOAuthCredentials(oss3Config.getApi_key(), oss3Config.getService_instance_id());
	
	        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
	        clientConfig.setUseTcpKeepAlive(true);
	
	        cosClient = AmazonS3ClientBuilder.standard()
	        		                         .withCredentials(new AWSStaticCredentialsProvider(credentials))
	                                         .withEndpointConfiguration(new EndpointConfiguration(oss3Config.getEndpoint_url(), oss3Config.getLocation()))
	                                         .withPathStyleAccessEnabled(true)
	                                         .withClientConfiguration(clientConfig).build();
    	}
    	return cosClient;

    }

    /**
     * @param bucketName
     * @throws OSS3Exception 
     */
    public  ObjectListing listObjects(String bucketName) throws OSS3Exception{	
    	ObjectListing objectListing = null;
    	try {
    		
    		objectListing = this.createClient().listObjects(new ListObjectsRequest().withBucketName(bucketName));
    		
    		for(S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.printf("Item: %s (%s bytes)\n", objectSummary.getKey(), objectSummary.getSize());
            }
    		
		} catch (Exception e) {
			throwOSS3Exception("Erro in listObjects", e);
		}
        return objectListing;
    }
    
    /**
     * 
     * @param bucketName
     * @param objectName
     * @return
     * @throws OSS3Exception
     */
    public FileOutputStream getObjectOutputStream(String bucketName,String objectName) throws OSS3Exception {
    	FileOutputStream fos = null;
    	try {
	    	S3Object obj = this.createClient().getObject(bucketName, objectName);
	        S3ObjectInputStream s3is = obj.getObjectContent();
		
			fos = new FileOutputStream(new File(objectName));
		
	        byte[] read_buf = new byte[1024];
	        int read_len = 0;
	        while ((read_len = s3is.read(read_buf)) > 0) {
	            fos.write(read_buf, 0, read_len);
	        }
	        s3is.close();
	        fos.close();
		} catch (IOException e) {
			throwOSS3Exception("Erro in getObject", e);
		}
    	return fos;
	    
    }
 

    /**
     * @param bucketName
     * @param cosClient
     * @param storageClass
     * @throws OSS3Exception 
     */
    @SuppressWarnings("deprecation")
	public  Bucket createBucket(String bucketName ) throws OSS3Exception{
    	Bucket bucket =  null;
    	try {
    		bucket = this.createClient().createBucket(bucketName, oss3Config.getStorageClass());
		} catch (Exception e) {
			throwOSS3Exception("Erro in createBucket", e);
		}
    	return bucket;
    }

    /**
     * @param cosClient
     * @throws OSS3Exception 
     */
    public  List<Bucket> listBuckets() throws OSS3Exception{
    	List<Bucket> buckets = null;
    	try {
    		buckets = this.createClient().listBuckets();
		} catch (Exception e) {
			throwOSS3Exception("Erro in listBuckets", e);
		}
        return buckets ;
    }
    /**
     * 
     * @param fileText
     * @return
     * @throws OSS3Exception
     */
    public PutObjectResult putTextFile(FileS3DTO fileText) throws OSS3Exception {
    	PutObjectResult objectResult = null;
    	try {
	        InputStream newStream = new ByteArrayInputStream(fileText.getFileText().getBytes(StandardCharsets.UTF_8));
	
	        ObjectMetadata metadata = new ObjectMetadata();        
	        metadata.setContentLength(fileText.getFileText().length());
	
	        PutObjectRequest req = new PutObjectRequest(fileText.getBucketName(), fileText.getItemName(), newStream, metadata);
	        objectResult =  this.createClient().putObject(req);
    	}catch (Exception e) {
    		throwOSS3Exception("Erro in putTextFile", e);
		}
        return objectResult;
    }
    
    /**
     * 
     * @param fileS3
     * @return
     * @throws OSS3Exception 
     */
    public PutObjectResult putFile(FileS3DTO fileS3) throws OSS3Exception { 
    	 PutObjectResult objectResult = null;
    	try {
    		objectResult =  this.createClient().putObject(fileS3.getBucketName(), fileS3.getItemName(),fileS3.getFile() );
		} catch (Exception e) {
			throwOSS3Exception("Erro in putFile", e);
		}
        return objectResult;
    }
    
    /**
     * 
     * @param obj
     * @param bucketName
     * @param serializedObject
     * @return
     * @throws OSS3Exception
     */
    public PutObjectResult putObjectSerializable(Object obj,String bucketName,String serializedObject) throws OSS3Exception {
  
    	PutObjectResult objectResult = null;
    	try {
	    	ByteArrayOutputStream theBytes = new ByteArrayOutputStream(); 
	    	
	        ObjectOutputStream serializer = new ObjectOutputStream(theBytes); 
			serializer.writeObject(obj);			
	    	serializer.flush();
	    	serializer.close();
	    	
	    	InputStream stream = new ByteArrayInputStream(theBytes.toByteArray()); 
	    	ObjectMetadata metadata = new ObjectMetadata();
	    	
	    	metadata.setContentType("application/x-java-serialized-object"); 
	    	metadata.setContentLength(theBytes.size()); 
	    	
	    	this.createClient().putObject(bucketName, serializedObject, stream,  metadata );
    	} catch (IOException e) {
    		throwOSS3Exception("Erro in putObjectSerializable", e);
		} 
    	
    	return objectResult;
    }
    
    /**
     * 
     * @param bucketName
     * @param itemName
     * @param filePath
     * @throws OSS3Exception
     */
    public void multiPartUpload(String bucketName, String itemName, String filePath) throws OSS3Exception {
        File file = new File(filePath);
        if (!file.isFile()) {
            return;
        }

        InitiateMultipartUploadResult mpResult = createClient().initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, itemName));
        String uploadID = mpResult.getUploadId();

        //begin uploading the parts
        //min 5MB part size
        long partSize = 1024 * 1024 * 5;
        long fileSize = file.length();
        List<PartETag> dataPacks = new ArrayList<PartETag>();

        try {
            long position = 0;
            for (int partNum = 1; position < fileSize; partNum++) {
                partSize = Math.min(partSize, (fileSize - position));
 

                UploadPartRequest upRequest = new UploadPartRequest()
                        .withBucketName(bucketName)
                        .withKey(itemName)
                        .withUploadId(uploadID)
                        .withPartNumber(partNum)
                        .withFileOffset(position)
                        .withFile(file)
                        .withPartSize(partSize);

                UploadPartResult upResult = createClient().uploadPart(upRequest);
                dataPacks.add(upResult.getPartETag());

                position += partSize;
            } 
            createClient().completeMultipartUpload(new CompleteMultipartUploadRequest(bucketName, itemName, uploadID, dataPacks));
        } catch (SdkClientException e) {
        	this.createClient().abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, itemName, uploadID));
        	throwOSS3Exception("Erro in multiPartUpload", e);
        }
    }
    
    /**
     * 
     * @param bucketName
     * @param fileName
     * @return
     * @throws OSS3Exception 
     */
    public ObjectMetadata downloadFile(String bucketName,String fileName) throws OSS3Exception {
    	GetObjectRequest request = new GetObjectRequest( bucketName,fileName);
    	ObjectMetadata objectMetadata = null;
    	try {
    		objectMetadata =  createClient().getObject( request, new File("retrieved.txt") );
		} catch (Exception e) {
			throwOSS3Exception("Erro in downloadFile", e);
		}
    	return objectMetadata;
    }
    /**
     * 
     * @param bucketName
     * @param objectName
     * @return
     * @throws OSS3Exception
     */
    public S3ObjectInputStream  objectStreem(String bucketName,String objectName) throws OSS3Exception {
    	S3ObjectInputStream s3ObjectInputStream = null;
    	try {
    		S3Object s3PbjectResponse = createClient().getObject( bucketName, objectName );
    		s3ObjectInputStream = s3PbjectResponse.getObjectContent();
		} catch (Exception e) {
			throwOSS3Exception("Erro in objectStreem", e);
		}
    	return s3ObjectInputStream;
    }
    
    /**
     * 
     * @param bucketName
     * @param itemName
     * @throws OSS3Exception
     */
    public void deleteItem(String bucketName, String itemName) throws OSS3Exception {
    	try {
    		createClient().deleteObject(bucketName, itemName);
    	}catch (Exception e) {
    		throwOSS3Exception("Erro in deleteItem", e);
		}
       
    }
    
    /**
     * 
     * @param bucketName
     * @throws OSS3Exception 
     */
    public  void deleteBucket(String bucketName) throws OSS3Exception {
    	try {
         createClient().deleteBucket(bucketName);
    	}catch (Exception e) {
    		throwOSS3Exception("Erro in deleteBucket", e);
		}
    }
    
    private void throwOSS3Exception(String msg,Exception e) throws OSS3Exception {
    	LOG.error(msg+" :"+e.getMessage(), e);
		throw new OSS3Exception(msg, e);
    }


}