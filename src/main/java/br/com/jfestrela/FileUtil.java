package br.com.jfestrela;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import br.com.jfestrela.exception.OSS3Exception;

public final class  FileUtil {
	
	private FileUtil() {};
	
	public static File writeFile(byte[] data,String fileName) throws OSS3Exception {
		File file = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        OutputStream os;
		try {
			os = new FileOutputStream(file);
		    os.write(data); 
		    os.close(); 

		} catch (IOException e) {
			throw new OSS3Exception("Error in loadFile ", e);
		} 
		return file;
	}

}
