package br.com.jfestrela.config;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class OSS3Config implements Serializable{

	private static final long serialVersionUID = -3247797927016190848L;
	
	@ConfigProperty(name = "os.s3.iam.endpopint")
	private String iamEndPoint ;
	@ConfigProperty(name = "os.s3.bucket.name")
    private String bucketName ;  // eg my-unique-bucket-name
	@ConfigProperty(name = "os.s3.bucket.new.name")
    private String newBucketName ; // eg my-other-unique-bucket-name
	@ConfigProperty(name = "os.s3.api.key")
    private String api_key ; // eg "W00YiRnLW4a3fTjMB-oiB-2ySfTrFBIQQWanc--P3byk"
	@ConfigProperty(name = "os.s3.service.instance.id")
    private String service_instance_id ; // eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"
	@ConfigProperty(name = "os.s3.endpoint.url")
    private String endpoint_url ; // this could be any service endpoint
	@ConfigProperty(name = "os.s3.storage.class")
    private String storageClass ;
	@ConfigProperty(name = "os.s3.location")
    private String location ;
	
	public String getIamEndPoint() {
		return iamEndPoint;
	}
	public void setIamEndPoint(String iamEndPoint) {
		this.iamEndPoint = iamEndPoint;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getNewBucketName() {
		return newBucketName;
	}
	public void setNewBucketName(String newBucketName) {
		this.newBucketName = newBucketName;
	}
	public String getApi_key() {
		return api_key;
	}
	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}
	public String getService_instance_id() {
		return service_instance_id;
	}
	public void setService_instance_id(String service_instance_id) {
		this.service_instance_id = service_instance_id;
	}
	public String getEndpoint_url() {
		return endpoint_url;
	}
	public void setEndpoint_url(String endpoint_url) {
		this.endpoint_url = endpoint_url;
	}
	public String getStorageClass() {
		return storageClass;
	}
	public void setStorageClass(String storageClass) {
		this.storageClass = storageClass;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	} 
}
	