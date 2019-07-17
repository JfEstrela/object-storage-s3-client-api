package br.com.jfestrela.dto;

import java.io.File;
import java.io.Serializable;

public class FileS3DTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String bucketName,itemName, fileText;
	
	private File file;

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getFileText() {
		return fileText;
	}

	public void setFileText(String fileText) {
		this.fileText = fileText;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
	