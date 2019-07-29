package br.com.jfestrela.exception;

public class OSS3Exception extends Exception{

	private static final long serialVersionUID = 889408425024878610L;

	public OSS3Exception() {
		super();
	}

	public OSS3Exception(String message, Throwable cause) {
		super(message, cause);
	}

	public OSS3Exception(String message) {
		super(message);

	}

	public OSS3Exception(Throwable cause) {
		super(cause);
	}
	
	

}
